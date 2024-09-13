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