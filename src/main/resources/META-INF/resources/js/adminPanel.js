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

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('cadastroChamadosForm');

    if (form) {
        form.addEventListener('submit', function (event) {
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
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erro ao cadastrar o chamado');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        const messageDiv = document.getElementById('message');
                        messageDiv.textContent = 'Chamado cadastrado com sucesso! Fechando...';
                        messageDiv.style.display = 'block';

                        setTimeout(function () {
                            modalCalled.style.display = "none";
                        }, 3000);
                    } else {
                        alert('Erro ao cadastrar o chamado' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Erro: ', error);
                    alert('Ocorreu um erro no cadastro');
                });
        });
    } else {
        console.error('Elemento com ID cadastroChamadosForm não encontrado.');
    }
});


document.addEventListener('DOMContentLoaded', function () {
    // Inicializar as iniciais do usuário logado
    const userFullName = 'Matheus Meireles Guerra'; // Exemplo, você deve obter isso do backend
    const initials = getInitials(userFullName);
    const profileInitialDiv = document.getElementById('profile-initials');

    if (profileInitialDiv) {
        profileInitialDiv.textContent = initials;
    } else {
        console.error('Elemento profile-initials não encontrado!');
    }

    // Mostrar/Esconder menu de logout ao clicar no perfil
    const profile = document.querySelector('.profile');
    const logoutMenu = document.getElementById('logout-menu');

    if (profile && logoutMenu) {
        profile.addEventListener('click', function () {
            if (logoutMenu.style.display === 'none' || logoutMenu.style.display === '') {
                logoutMenu.style.display = 'block';
            } else {
                logoutMenu.style.display = 'none';
            }
        });
    } else {
        console.error('Elemento profile ou logout-menu não encontrado!');
    }

    // Lógica de deslogar
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function () {
           logoutUsuario();
        });
    } else {
        console.error('Elemento logout-btn não encontrado!');
    }
});

// Função para capturar as iniciais do nome completo
function getInitials(fullName) {
    const nameParts = fullName.split(' ');
    const firstName = nameParts[0];
    const lastName = nameParts[nameParts.length - 1];
    return `${firstName[0]}${lastName[0]}`.toUpperCase();
}

// Função de logout (que chamará o método na classe UsuarioBO)
function logoutUsuario() {
    fetch('/deslogar', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                return response.json();// Chama o endpoint de logout
            } else {
                throw new Error('Erro ao deslogar. Tente novamente.');
            }
        })
        .then(data => {
            if (data.statusResposta === 200) {
                window.location.href = data.url;  // Redireciona para a página de login
            } else {
                alert('Erro ao deslogar. Tente novamente.');
            }
        })
        .catch(error => {
            console.error('Erro ao deslogar:', error);
            alert(error.message);
        });
}