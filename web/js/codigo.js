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
        var obj = JSON.parse(evt.data),
            msg = 'Nombre ' + obj.nombre + ' dice: ' + obj.mensaje;
        mensajes.innerHTML += '<br/>' + msg;
    }
})(window, document, JSON);