var modal = document.getElementById("myModal");
var btn = document.getElementById("open-called");
var span = document.getElementsByClassName("close")[0];
var form = document.querySelector("form");
var sendButton = document.getElementById("send-call");

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

form.onsubmit = function(event) {
    event.preventDefault(); // Impede o comportamento padrão de envio do formulário

    // Captura os dados do formulário
    var title = document.getElementById("title").value;
    var description = document.getElementById("description").value;

    // Verifica se os campos estão preenchidos
    if (!title || !description) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    // Dados do chamado a serem enviados
    var chamadoData = {
        titulo: title,
        descricao: description,
    };

    // Envia os dados para a API
    fetch('/chamados', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(chamadoData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Erro ao enviar chamado: ' + response.statusText);
            }
        })
        .then(data => {
            alert('Chamado criado com sucesso!');
            modal.style.display = "none"; // Fecha o modal
            form.reset(); // Limpa o formulário
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao criar chamado. Por favor, tente novamente.');
        });
}