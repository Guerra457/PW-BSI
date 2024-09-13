// Modal de usuários
/*var modalUsers = document.getElementById("modal-user");
var btnUsers = document.getElementById("open-user"); // Ajuste para associar o botão certo
var spanUsers = document.querySelector("#modal-user .close");

btnUsers.onclick = function() {
    modalUsers.style.display = "block";
}

spanUsers.onclick = function() {
    modalUsers.style.display = "none";
}*/

// Modal de chamados
var modalCalled = document.getElementById("modal-called");
var btnCalled = document.getElementById("open-called"); // Ajuste se necessário para associar ao botão correto
var spanCalled = document.querySelector("#modal-called .close");

btnCalled.onclick = function() {
    modalCalled.style.display = "block";
}

spanCalled.onclick = function() {
    modalCalled.style.display = "none";
}

// Fechar o modal ao clicar fora dele
window.onclick = function(event) {
    if (event.target === modalCalled) {
        modalCalled.style.display = "none";
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const userList = document.getElementById('user-list');
    const noResultMessage = document.querySelector('.result-item p');

    // Função para carregar usuários
    function carregarUsuarios() {
        fetch('/usuarios')  // Chama o endpoint Java
            .then(response => {
                console.log('Resposta recebida:', response);
                if (!response.ok) {
                    throw new Error('Erro na requisição: ' + response.status);
                }
                return response.json();  // Converte a resposta para JSON
            })
            .then(data => {
                console.log('Dados recebidos:', data)
                if (data.length > 0) {
                    userList.innerHTML = ''; // Limpa a lista de usuários
                    noResultMessage.style.display = 'none'; // Esconde a mensagem "Nenhum item encontrado"

                    // Itera sobre os dados dos usuários e cria elementos HTML
                    data.forEach(usuario => {
                        const userItem = document.createElement('div');
                        userItem.classList.add('user-item');
                        userItem.innerHTML = `
                            <p><strong>Nome:</strong> ${usuario.nome}</p>
                            <p><strong>CPF:</strong> ${usuario.cpf}</p>
                            <p><strong>Email:</strong> ${usuario.email}</p>
                            <p><strong>Tipo de Usuário:</strong> ${usuario.tipoUsuario}</p>
                        `;
                        userList.appendChild(userItem);  // Adiciona o usuário à lista
                    });
                } else {
                    noResultMessage.style.display = 'block'; // Exibe a mensagem se não houver usuários
                    userList.innerHTML = ''; // Limpa a lista
                }
            })
            .catch(error => {
                console.error('Erro ao carregar usuários:', error);
            });
    }

    // Carregar usuários assim que a página for carregada
    carregarUsuarios();
});
