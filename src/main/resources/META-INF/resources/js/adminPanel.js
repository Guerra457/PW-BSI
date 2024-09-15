document.addEventListener('DOMContentLoaded', function () {
    carregarUsuarios();  // Certifique-se que a função é chamada ao carregar a página

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
});
function carregarUsuarios() {
    fetch('/usuarios')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            const userList = document.getElementById('user-list');
            const noItemsMessage = document.querySelector('.result-item');

            userList.innerHTML = '';  // Limpa qualquer conteúdo anterior

            if (data.length === 0) {
                noItemsMessage.style.display = 'block';
            } else {
                noItemsMessage.style.display = 'none';

                data.forEach(usuario => {
                    const userItem = document.createElement('div');
                    userItem.classList.add('user-item');

                    const cpfFormatado = adicionarFormatacaoCPF(usuario.cpf);

                    userItem.innerHTML = `
                    <p><strong>Nome:</strong> ${usuario.nome}</p>
                    <p><strong>CPF:</strong> ${cpfFormatado}</p>
                    <p><strong>Email:</strong> ${usuario.email}</p>
                    <p><strong>Tipo de Usuário:</strong> ${usuario.tipoUsuario}</p>
                    <div class="user-buttons">
                        <button class="update-button" data-id="${usuario.id}">✏️</button>
                        <button class="delete-button" data-id="${usuario.id}">❌</button>
                    </div>
                `;
                    userList.appendChild(userItem);  // Adiciona o usuário à lista
                });
            }
        })
        .catch(error => {
            console.error('Erro ao carregar usuários:', error);
            const userList = document.getElementById('user-list');
            userList.innerHTML = '<p>Erro ao carregar usuários. Tente novamente mais tarde</p>';
        });
}

function adicionarFormatacaoCPF(cpf) {
    if (cpf.length === 11) {
        // Aplica a máscara XXX.XXX.XXX-XX
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    } else {
        return cpf; // Retorna o CPF sem modificação se não tiver 11 dígitos
    }
}
