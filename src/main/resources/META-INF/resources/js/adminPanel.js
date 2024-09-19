document.addEventListener('DOMContentLoaded', function () {
    carregarUsuarios();
    carregarChamados();
    inicializarModais();
    inicializarFormularioChamado();
    inicializarPerfilUsuario();
});

function inicializarModais() {
    var modalCalled = document.getElementById("modal-called");
    var btnCalled = document.getElementById("open-called"); // Ajuste se necessário para associar ao botão correto
    var spanCalled = document.querySelector("#modal-called .close");

    if (btnCalled) {
        btnCalled.onclick = function() {
            modalCalled.style.display = "block";
        };
    }

    if (spanCalled) {
        spanCalled.onclick = function() {
            modalCalled.style.display = "none";
        };
    }

// Fechar o modal ao clicar fora dele
    window.onclick = function(event) {
        if (event.target === modalCalled) {
            modalCalled.style.display = "none";
        }
    }
}
function carregarUsuarios() {
    fetch('/usuarios/lista-usuarios')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição de usuários: ' + response.status);
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
                        <button class="update-button" data-id="${usuario.idUsuario}">✏️</button>
                        <button class="delete-button" data-id="${usuario.idUsuario}">❌</button>
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

function carregarChamados() {
    fetch('/chamados/lista-chamados')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição de chamados: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            const calledList = document.getElementById('called-list');
            const noItemsMessage = document.querySelector('.result-item');

            calledList.innerHTML = '';

            if (data.length === 0) {
                noItemsMessage.style.display = 'block';
            } else {
                noItemsMessage.style.display = 'none';

                data.forEach(chamado => {
                    const calledItem = document.createElement('div');
                    calledItem.classList.add('called-item');



                    calledItem.innerHTML = `
                    <p><strong>Título:</strong> ${chamado.titulo}</p>
                    <p><strong>Descrição:</strong> ${chamado.descricao}</p>
                    <p><strong>Aberto por:</strong> ${chamado.nomeSolicitante}</p>
                    <p><strong>Atribuído à:</strong> ${chamado.idAtendente || 'Não atribuído'}</p>
                    <p><strong>Status:</strong> ${chamado.nomeStatus}</p>
                    <div class="called-buttons">
                        <button class="update-button-called" data-id="${chamado.idChamado}">✏️</button>
                    </div>
                    `;
                    calledList.appendChild(calledItem);
                });

                associarEventosModal();
            }
        })
        .catch(error => {
           console.error('Erro ao carregar chamados:', error);
           const calledList = document.getElementById('called-list');
           calledList.innerHTML = '<p>Erro ao carregar chamados. Tente novamente mais tarde</p>';
        });
}

function associarEventosModal() {
    var modalUpdate = document.getElementById("modal-update");
    var modalDelete = document.getElementById("modal-delete");
    var modalUpdateCalled = document.getElementById("modal-update-called");

    var updateButtons = document.getElementsByClassName("update-button");
    var deleteButtons = document.getElementsByClassName("delete-button");
    var updateButtonsCalled = document.getElementsByClassName("update-button-called");

    var spanUpdate = document.querySelector("#modal-update .close");
    var spanDelete = document.querySelector("#modal-delete .close");
    var spanUpdateCalled = document.querySelector("#modal-update-called .close");

    Array.from(updateButtons).forEach(button => {
        button.onclick = function() {
            userIdToUpdate = this.getAttribute("data-id");
            carregarDadosUsuario(userIdToUpdate);
            modalUpdate.style.display = "block";
        };
    });


    Array.from(deleteButtons).forEach(button => {
        button.onclick = function() {
            var userId = this.getAttribute("data-id");
            modalDelete.style.display = "block";
            // Atualiza o data-id do botão de confirmação
            var deleteButton = document.getElementById('confirm-delete');
            deleteButton.setAttribute('data-id', userId);
        };
    });

    Array.from(updateButtonsCalled).forEach(button => {
        button.onclick = function () {
            calledIdToUpdate = this.getAttribute("data-id");
            carregarDadosChamado(calledIdToUpdate);
            modalUpdateCalled.style.display = "block";
        }
    })

    // Eventos para fechar os modais
    spanUpdate.onclick = function () {
        modalUpdate.style.display = "none";
    }

    spanDelete.onclick = function () {
        modalDelete.style.display = "none";
    }

    spanUpdateCalled.onclick = function () {
        modalUpdateCalled.style.display = "none";
    }

    // Fechar o modal ao clicar fora dele
    window.onclick = function(event) {
        if (event.target === modalUpdate) {
            modalUpdate.style.display = "none";
        }
        if (event.target === modalDelete) {
            modalDelete.style.display = "none";
        }
        if (event.target === modalUpdateCalled) {
            modalUpdateCalled.style.display = "none";
        }
    }



    // Evento de confirmação de deleção
    document.getElementById('confirm-delete')?.addEventListener('click', function() {
        var userId = this.getAttribute('data-id');
        if (userId) {
            deletarUsuario(userId);
            modalDelete.style.display = "none"; // Fecha o modal após deleção
        } else {
            console.error('ID do usuário não encontrado.');
        }
    });

    const formUpdate = document.getElementById('update-user-form');
    if (formUpdate) {
        formUpdate.removeEventListener('submit', lidarComAtualizacao);
        formUpdate.addEventListener('submit', lidarComAtualizacao);
    }

    const formUpdateCalled = document.getElementById('update-chamados-form');
    if (formUpdateCalled) {
        formUpdateCalled.removeEventListener('submit', lidarComAtualizacaoCalled);
        formUpdateCalled.addEventListener('submit', lidarComAtualizacaoCalled);
    }
}

function lidarComAtualizacao(event) {
    event.preventDefault();
    atualizarUsuario(userIdToUpdate);
}

function lidarComAtualizacaoCalled(event) {
    event.preventDefault();
    atualizarChamado(calledIdToUpdate);
}

function carregarDadosChamado(id) {
    console.log("Id recebido para carregar dados do chamado:", id);
    fetch(`/chamados/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: '+ response.status);
            }
            return response.json();
        })
        .then(chamado => {
            console.log("Dados do chamado:", chamado);

            document.getElementById('titulo').value = chamado.titulo;
            document.getElementById('descricao').value = chamado.descricao;
            document.getElementById('solicitante').value = chamado.nomeSolicitante;
            document.getElementById('atendente').value = chamado.nomeAtendente;
            document.getElementById('attStatus').value = chamado.nomeStatus;
        })
        .catch(error => {
            console.error('Erro ao carregar dados do chamado:', error);
        });
}

function carregarDadosUsuario(id) {
    fetch(`/usuarios/${id}`)
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

function inicializarFormularioChamado() {
    const form = document.getElementById('cadastroChamadosForm');
    const modalCalled = document.getElementById('modal-called');
    const messageDiv = document.getElementById('message');

    if (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault();

            const formData = new FormData(this);
            const data = {};
            formData.forEach((value, key) => {
                data[key] = value;
            });

            console.log('Dados do formulário:', data);

            fetch('/chamados', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(errData => {
                            throw new Error(errData.message || 'Erro ao cadastrar o chamado');
                        })
                    }
                    return response.json();
                })
                .then(data => {
                    //const messageDiv = document.getElementById('message');
                    messageDiv.textContent = 'Chamado cadastrado com sucesso! Fechando...';
                    messageDiv.style.display = 'block';

                    setTimeout(function () {
                        modalCalled.style.display = "none";
                        messageDiv.style.display = "none";
                    }, 2000);
                    carregarChamados()
                })
                .catch(error => {
                    console.error('Erro: ', error);
                    alert('Ocorreu um erro no cadastro');
                });

        });
    } else {
        console.error('Elemento com ID cadastroChamadosForm não encontrado.');
    }
}

function inicializarPerfilUsuario() {
    fetch('/usuarios/logado')
        .then(response => {
            if (!response.ok) {
                throw new Error('Não foi possível obter os dados do usuário');
            }
            return response.json();
        })
        .then(data => {
            // Inicializar as iniciais do usuário logado
            const userFullName = data.nome; // Exemplo, você deve obter isso do backend
            const initials = getInitials(userFullName);
            const profileInitialDiv = document.getElementById('profile-initials');

            if (profileInitialDiv) {
                profileInitialDiv.textContent = initials;
            } else {
                console.error('Elemento profile-initials não encontrado!');
            }
        })
        .catch(error => {
            console.error('Erro ao obter dados do usuário: ', error);
        });

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
}

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

function deletarUsuario(id) {
    fetch(`/usuarios/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || 'Erro ao deletar usuário');
            });
        }

        if (response.status === 204) {
            return;
        }

        return response.json();
    })
    .then(data => {
        carregarUsuarios();
    })
    .catch(error => {
        console.error('Erro ao deletar o usuário:', error);
    });
}

function atualizarUsuario(id) {
    const nome = document.getElementById('attName').value;
    const cpf = document.getElementById('attCPF').value.replace(/[.\-]/g, '');
    const email = document.getElementById('attEmail').value;
    const tipoUsuario = document.getElementById('attUserType').value;

    console.log("Dados enviados:", { nome, cpf, email, tipoUsuario });

    fetch(`/usuarios/update/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nome: nome,
            cpf: cpf,
            email: email,
            tipoUsuario: tipoUsuario
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || 'Erro ao atualizar usuário');
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Usuário atualizado com sucesso:', data);
        carregarUsuarios();
        document.getElementById('modal-update').style.display = 'none';

    })
    .catch(error => {
        console.error('Erro ao atualizar o usuário: ', error);
        alert(error.message);
    });
}

function atualizarChamado(id){
    console.log("id do chamado que será atualizado:", id);

    const titulo = document.getElementById('titulo').value;
    const descricao = document.getElementById('descricao').value;
    const solicitante = document.getElementById('solicitante').value;
    const atendente = document.getElementById('atendente').value;
    const status = document.getElementById('attStatus').value;

    console.log("Dados enviados:", {titulo, descricao, solicitante, atendente, status});

    fetch(`/chamados/update/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify( {
            titulo: titulo,
            descricao: descricao,
            solicitante: solicitante,
            atendente: atendente,
            nomeStatus: status
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Erro ao atualizar chamado');
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Chamado atualizado com sucesso:', data);
            carregarChamados();
            document.getElementById('modal-called').style.display = 'none';
        })
        .catch(error => {
            console.error('Erro ao atualizar o usuário: ', error);
            alert(error.message);
        });
}