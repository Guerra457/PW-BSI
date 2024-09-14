document.getElementById("backToLoginButton").onclick = function() {
    window.location.href = "/login";
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

document.getElementById('cadastroForm').addEventListener('submit', function (event) {
    event.preventDefault();  // Impede o envio padrão do formulário

    const formData = new FormData(this);
    const data = {};
    formData.forEach((value, key) => {
       data[key] = value;
    });

    fetch('/cadastro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const messageDiv = document.getElementById('message');
                messageDiv.textContent = 'Usuário cadastrado com sucesso! Redirecionando...';
                messageDiv.style.display = 'block'; // Torna a mensagem visível

                // Redireciona para a página de login após 3 segundos
                setTimeout(function () {
                    window.location.href = "/login";
                }, 3000);
            } else {
                alert('Erro ao cadastrar o usuário.');
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Ocorreu um erro no cadastro.');
        });
});