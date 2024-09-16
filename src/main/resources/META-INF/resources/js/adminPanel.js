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

                associarEventosModal();
            }
        })
        .catch(error => {
            console.error('Erro ao carregar usuários:', error);
            const userList = document.getElementById('user-list');
            userList.innerHTML = '<p>Erro ao carregar usuários. Tente novamente mais tarde</p>';
        });
}

function associarEventosModal() {
    var modalUpdate = document.getElementById("modal-update");
    var modalDelete = document.getElementById("modal-delete");

    var updateButtons = document.getElementsByClassName("update-button");
    var deleteButtons = document.getElementsByClassName("delete-button");

    var spanUpdate = document.querySelector("#modal-update .close");
    var spanDelete = document.querySelector("#modal-delete .close");

    // Loop para associar os eventos de clique aos botões de atualização
    for (var i = 0; i < updateButtons.length; i++) {
        updateButtons[i].onclick = function() {
            var userId = this.getAttribute("data-id");

            carregarDadosUsuario(userId);
            modalUpdate.style.display = "block";
        };
    }

    // Loop para associar os eventos de clique aos botões de deleção
    for (var i = 0; i < deleteButtons.length; i++) {
        deleteButtons[i].onclick = function() {
            modalDelete.style.display = "block";
        };
    }

    // Eventos para fechar os modais
    spanUpdate.onclick = function () {
        modalUpdate.style.display = "none";
    }

    spanDelete.onclick = function () {
        modalDelete.style.display = "none";
    }

    // Fechar o modal ao clicar fora dele
    window.onclick = function(event) {
        if (event.target === modalUpdate) {
            modalUpdate.style.display = "none";
        } else if (event.target === modalDelete) {
            modalDelete.style.display = "none";
        }
    }
}

function carregarDadosUsuario(Id) {
    fetch(`/usuarios/${Id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.status);
            }
            return response.json();
        })
        .then(usuario => {
            // Preenche os campos do modal com os dados do usuário
            document.getElementById('attName').value = usuario.nome;
            document.getElementById('attCPF').value = adicionarFormatacaoCPF(usuario.cpf);
            document.getElementById('attEmail').value = usuario.email;
            document.getElementById('attUserType').value = usuario.tipoUsuario; // Certifique-se que o select tem esse ID
        })
        .catch(error => {
            console.error('Erro ao carregar dados do usuário:', error);
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

document.getElementById('cadastroChamadosForm').addEventListener('submit', function (event){
    event.preventDefault();

    const formData = new FormData(this);
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    fetch('/chamados', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const messageDiv = document.getElementById('message');
                messageDiv.textContent = 'Chamado cadastrado com sucesso! fechando...';
                messageDiv.style.display = 'block';

                setTimeout(function () {
                    modalCalled.style.display = "none";
                }, 3000);
            } else {
                alert('Erro ao cadastrar o chamado');
            }
        })
        .catch(error => {
            console.error('Erro: ', error);
            alert('Ocorreu um erro no cadastro');
        });
});