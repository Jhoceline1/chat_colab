(function(window, document, JSON){
    'use strict';
    
    var url = 'ws://' + window.location.host + '/chat_colab/chat_colab',
        ws = new WebSocket(url),
        mensajes = document.getElementById('conversacion'),
        boton = document.getElementById('btnEnviar'),
        nombre = document.getElementById('usuario'),
        mensaje = document.getElementById('mensaje');

    ws.onopen = onOpen;
    ws.onclose = onClose;
    ws.onmessage = onMessage;
    boton.addEventListener('click', enviar);

    function onOpen(){
        console.log('Conectado...');
    }

    function onClose(){
        console.log('Desconectado...');
    }

    function enviar(){
        var msg = {
            nombre: nombre.value,
            mensaje: mensaje.value
        };
        ws.send(JSON.stringify(msg));
        mensaje.value = ""; 
    }

    function onMessage(evt){
        var obj = JSON.parse(evt.data);
        var nombreUsuario = obj.nombre;
        var texto = obj.mensaje;
        var icono = '⚫'; // Por defecto: mensaje de usuario

        if (nombreUsuario === "Sistema") {
            // Detectamos tipo de mensaje del sistema
            if (texto.includes("se ha unido")) {
                icono = '✅'; // Entrada
            } else if (texto.includes("ha salido")) {
                icono = '❌'; // Salida
            } else {
                icono = 'ℹ️'; // Otros mensajes del sistema
            }

            mensajes.innerHTML += `<br/><em>${icono} ${texto}</em>`;
        } else {
            mensajes.innerHTML += `<br/>${icono} <strong>${nombreUsuario}:</strong> ${texto}`;
        }
    }
})(window, document, JSON);
