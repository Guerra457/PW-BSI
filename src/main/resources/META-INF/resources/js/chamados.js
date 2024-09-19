document.addEventListener('DOMContentLoaded', function () {
    carregarChamados();
    inicializarModais();
    inicializarPerfilUsuario();

});

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
                            <button class="update-button" data-id="${chamado.idChamado}">✏️</button>
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


function inicializarModais() {
    var modalCalled = document.getElementById("modal-called");
    var spanUpdate = document.querySelector("#modal-called .close");

    if (spanUpdate) {
        spanUpdate.onclick = function () {
            modalCalled.style.display = "none";
        };
    }

    window.onclick = function(event) {
        if (event.target === modalCalled) {
            modalCalled.style.display = "none";
        }
    }
}

let calledIdToUpdate;
function associarEventosModal() {
    var modalCalled = document.getElementById("modal-called");
    var updateButtons = document.querySelectorAll(".update-button");

    Array.from(updateButtons).forEach(button => {
        button.onclick = function() {
            calledIdToUpdate = parseInt(this.getAttribute("data-id"), 10); // Obtém o ID do chamado
            console.log("O id do chamado é: " + calledIdToUpdate);

            carregarDadosChamado(calledIdToUpdate);
            modalCalled.style.display = "block"; // Mostra o modal
        };
    });

    const formUpdate = document.getElementById('update-chamados-form');
    if (formUpdate) {
        formUpdate.removeEventListener('submit', lidarComAtualizacao);
        formUpdate.addEventListener('submit', lidarComAtualizacao);
    }
}

function lidarComAtualizacao(event) {
    event.preventDefault();
    atualizarChamado(calledIdToUpdate);
}

function getInitials(fullName) {
    const nameParts = fullName.split(' ');
    const firstName = nameParts[0];
    const lastName = nameParts[nameParts.length - 1];
    return `${firstName[0]}${lastName[0]}`.toUpperCase();
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