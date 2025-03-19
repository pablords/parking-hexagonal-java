const { Pact } = require("@pact-foundation/pact");
const { like } = require("@pact-foundation/pact").Matchers;
const path = require("path");
const axios = require("axios");
const { Matchers } = require("@pact-foundation/pact");

const provider = new Pact({
  consumer: "ParkingApp",
  provider: "ParkingService",
  port: 1234,
  log: path.resolve(__dirname, "logs", "pact.log"),
  dir: path.resolve(__dirname, "pacts"),
  logLevel: "info",
});

describe("Consumer Pact Test - Car", () => {
  beforeAll(() => provider.setup());
  afterAll(() => provider.finalize());

  describe("Buscar carro pela placa", () => {
    beforeEach(() =>
      provider.addInteraction({
        state: "Existe um carro cadastrado com a placa ABC1234",
        uponReceiving: "Um request para buscar um carro pela placa",
        withRequest: {
          method: "GET",
          path: "/api/v1/cars/ABC1234",
          headers: {
            Accept: "application/json",
          },
        },
        willRespondWith: {
          status: 200,
          headers: {
            "Content-Type": "application/json",
          },
          body: {
            id: like("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"),
            brand: like("Toyota"),
            color: like("Red"),
            model: like("Sedan"),
            plate: like("ABC1234"),
          },
        },
      })
    );

    it("deve retornar os detalhes do carro", async () => {
      const response = await axios.get(
        `${provider.mockService.baseUrl}/api/v1/cars/ABC1234`,
        { headers: { Accept: "application/json" } }
      );

      expect(response.status).toEqual(200);
      expect(response.data).toMatchObject({
        id: expect.any(String),
        brand: expect.any(String),
        color: expect.any(String),
        model: expect.any(String),
        plate: expect.any(String),
      });
    });
  });

  afterEach(() => provider.verify());
});

describe("Consumer Pact Test - Check-in", () => {
  beforeAll(() => provider.setup());
  afterAll(() => provider.finalize());

  it("Realiza check-in de um carro", async () => {
    await provider.addInteraction({
      state: "Existe uma vaga disponível",
      uponReceiving: "Realiza check-in de um carro",
      withRequest: {
        method: "POST",
        path: "/api/v1/checkins",
        headers: { "Content-Type": "application/json" },
        body: {
          brand: "Toyota",
          color: "Red",
          model: "Sedan",
          plate: "ABC1234",
        },
      },
      willRespondWith: {
        status: 201,
        headers: { "Content-Type": "application/json" },
        body: Matchers.like({
          checkInTime: Matchers.regex({
            generate: "2025-03-13T13:00:00.000Z", // Valor gerado
            matcher:
              "^\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d{3})?Z$", // Expressão regular
          }),
          id: Matchers.uuid(),
          slot: {
            id: Matchers.integer(1),
            occupied: true,
          },
        }),
      },
    });

    const response = await axios.post(
      `${provider.mockService.baseUrl}/api/v1/checkins`,
      {
        brand: "Toyota",
        color: "Red",
        model: "Sedan",
        plate: "ABC1234",
      },
      { headers: { "Content-Type": "application/json" } }
    );

    expect(response.status).toBe(201);
    expect(response.data.slot.occupied).toBe(true);

    await provider.verify();
  });

  it("Retorna erro ao tentar realizar check-in de um carro com placa vazia", async () => {
    await provider.addInteraction({
      state: "Tentativa de check-in com placa vazia",
      uponReceiving:
        "Retorna erro ao tentar realizar check-in de um carro com placa vazia",
      withRequest: {
        method: "POST",
        path: "/api/v1/checkins",
        headers: { "Content-Type": "application/json" },
        body: {
          brand: "Toyota",
          color: "Red",
          model: "Sedan",
          plate: "",
        },
      },
      willRespondWith: {
        status: 422,
        headers: { "Content-Type": "application/json" },
        body: Matchers.like({
          error: "Unprocessable Entity",
          message: "Validation error",
          errors: { plate: "Plate cannot be empty" },
        }),
      },
    });

    try {
      await axios.post(`${provider.mockService.baseUrl}/api/v1/checkins`, {
        brand: "Toyota",
        color: "Red",
        model: "Sedan",
        plate: "",
      });
    } catch (error) {
      expect(error.response.status).toBe(422);
      expect(error.response.data.errors.plate).toBe("Plate cannot be empty");
    }

    await provider.verify();
  });

  it("Retorna erro ao tentar realizar check-in de um carro com placa inválida", async () => {
    await provider.addInteraction({
      state: "Tentativa de check-in com placa inválida",
      uponReceiving:
        "Retorna erro ao tentar realizar check-in de um carro com placa inválida",
      withRequest: {
        method: "POST",
        path: "/api/v1/checkins",
        headers: { "Content-Type": "application/json" },
        body: {
          brand: "Toyota",
          color: "Red",
          model: "Sedan",
          plate: "1234567890",
        },
      },
      willRespondWith: {
        status: 400,
        headers: { "Content-Type": "application/json" },
        body: Matchers.like({
          error: "Bad Request",
          message: "Invalid plate",
        }),
      },
    });

    try {
      await axios.post(`${provider.mockService.baseUrl}/api/v1/checkins`, {
        brand: "Toyota",
        color: "Red",
        model: "Sedan",
        plate: "1234567890",
      });
    } catch (error) {
      expect(error.response.status).toBe(400);
      expect(error.response.data.message).toBe("Invalid plate");
    }

    await provider.verify();
  });
});

describe("Pact Consumer Test - Checkout", () => {
  beforeAll(() => provider.setup());
  afterAll(() => provider.finalize());

  describe("Realiza check-out de um carro", () => {
    beforeAll(() => {
      return provider.addInteraction({
        state:
          "Existe um carro estacionado com a placa ABC1234 e pronto para checkout",
        uponReceiving: "Um pedido de checkout válido",
        withRequest: {
          method: "POST",
          path: "/api/v1/checkouts",
          headers: { "Content-Type": "application/json" },
          body: { plate: "ABC1234" },
        },
        willRespondWith: {
          status: 201,
          headers: { "Content-Type": "application/json" },
          body: {
            checkOutTime: Matchers.regex({
              generate: "2025-03-13T14:00:00.000Z", // Valor gerado
              matcher:
                "^\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d{3})?Z$", // Expressão regular
            }),
            checkin: {
              checkInTime: Matchers.regex({
                generate: "2025-03-13T13:00:00.000Z", // Valor gerado
                matcher:
                  "^\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d{3})?Z$", // Expressão regular
              }),
              id: Matchers.uuid(),
              slot: { id: Matchers.integer(), occupied: false },
            },
            parkingFee: Matchers.decimal(),
          },
        },
      });
    });

    it("Deve retornar sucesso ao realizar checkout", async () => {
      const response = await axios.post(
        `${provider.mockService.baseUrl}/api/v1/checkouts`,
        { plate: "ABC1234" },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      expect(response.status).toBe(201);
      expect(response.data).toHaveProperty("checkOutTime");
      expect(response.data.checkin).toHaveProperty("checkInTime");
      expect(response.data.checkin).toHaveProperty("id");
      expect(response.data.checkin.slot.occupied).toBe(false);
      expect(response.data.parkingFee).toBeGreaterThan(0);
    });
  });

  describe("Erro ao tentar realizar checkout sem check-in", () => {
    beforeAll(() => {
      return provider.addInteraction({
        state: "Tentativa de check-out sem data de check-in",
        uponReceiving: "Um pedido de checkout sem check-in",
        withRequest: {
          method: "POST",
          path: "/api/v1/checkouts",
          headers: { "Content-Type": "application/json" },
          body: { plate: "ABC1234" },
        },
        willRespondWith: {
          status: 400,
          headers: { "Content-Type": "application/json" },
          body: {
            error: "Bad Request",
            message: "Checkin time is missing",
            path: "/api/v1/checkouts",
            timestamp: Matchers.regex({
              generate: "2025-03-13T15:00:00.000Z", // Valor gerado
              matcher:
                "^\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d(?:\\.\\d{3})?Z$", // Expressão regular
            }),
          },
        },
      });
    });

    it("Deve retornar erro 400 para checkout sem check-in", async () => {
      try {
        await axios.post(
          `${provider.mockService.baseUrl}/api/v1/checkouts`,
          { plate: "ABC1234" },
          {
            headers: { "Content-Type": "application/json" },
          }
        );
      } catch (error) {
        expect(error.response.status).toBe(400);
        expect(error.response.data.message).toBe("Checkin time is missing");
      }
    });
  });

  afterEach(() => provider.verify());
});
