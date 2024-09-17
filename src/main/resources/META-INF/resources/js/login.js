document.getElementById("registerButton").onclick = function() {
    window.location.href = "/cadastro";
}

document.getElementById('togglePasswordIcon').addEventListener('click', function () {
    const passwordField = document.getElementById('senha');
    const icon = document.getElementById('togglePasswordIcon');

    // Verifica se a senha está oculta ou visível
    const passwordType = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordField.setAttribute('type', passwordType);

    // Altera o ícone dependendo do estado atual
    icon.src = passwordType === 'password' ? '/images/exibirSenha.png' : '/images/ocultarSenha.png';
});

document.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    fetch('/autenticarUsuario', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: document.getElementById('email').value,
            senha: document.getElementById('senha').value
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.message); });
            }
            return response.json();
        })
        .then(data => {
            console.log("Resposta do servidor: ", data);
            if (data.statusResposta === 200) {
                // Redireciona para a página correta com base no tipo de usuário
                window.location.href = data.url;
            } else {
                alert(data.message || 'Falha no login. Verifique suas credenciais.');
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Ocorreu um erro no login.');
        });
});