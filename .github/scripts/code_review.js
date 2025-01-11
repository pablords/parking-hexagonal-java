// .github/scripts/code_review.js
// Script que lê o diff.txt, chama a OpenAI API e salva a resposta em review_comments.txt

const fs = require("fs")
const process = require("process")

// Se Node estiver < 18, você precisaria de 'node-fetch':
// import fetch from 'node-fetch';

async function main() {
  try {
    const openaiApiKey = process.env.OPENAI_API_KEY;
    if (!openaiApiKey) {
      throw new Error(
        "OPENAI_API_KEY não encontrada nas variáveis de ambiente."
      );
    }

    // 1) Ler diff.txt
    const diffContent = fs.readFileSync("diff.txt", "utf8");

    // 2) Montar prompt
    const systemPrompt =
      "You are an expert software engineer and code reviewer.";
    const userPrompt = `Please review the following diff. Mention possible improvements, bugs, or best practices:\n\n${diffContent}`;

    // 3) Configurar corpo da requisição
    // o1-mini	OpenAI's smallest model, fine-tuned on a mix of data sources.
    // gpt-3.5-turbo	OpenAI's largest model, fine-tuned on a mix of data sources.
    const model = "o1-mini";
    const body = {
      model: model,
      messages: [
        { role: "system", content: systemPrompt },
        { role: "user", content: userPrompt },
      ],
      max_tokens: 1024,
      temperature: 0.3,
    };

    // 4) Chamar API da OpenAI
    const response = await fetch("https://api.openai.com/v1/chat/completions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${openaiApiKey}`,
      },
      body: JSON.stringify(body),
    });

    // 5) Tratar resposta
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(
        `Falha ao chamar OpenAI API: ${response.status} - ${errorText}`
      );
    }

    const data = await response.json();
    const review =
      data.choices?.[0]?.message?.content || "Sem resposta do modelo.";

    // 6) Salvar em review_comments.txt
    fs.writeFileSync("review_comments.txt", review, "utf8");
    console.log("OpenAI review salvo em review_comments.txt com sucesso!");
  } catch (error) {
    console.error("Erro ao processar code review:", error);
    process.exit(1); // para o workflow marcar como falha
  }
}

main();
