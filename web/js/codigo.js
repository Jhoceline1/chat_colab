(function(window, document, JSON){
    'use strict';
    
    var url = 'ws://' + window.location.host + '/chat_colab/chat_colab',
        ws = new WebSocket(url),
        mensajes = document.getElementById('conversacion'),
        boton = document.getElementById('btnEnviar'),
        nombre = document.getElementById('usuario'),
        mensaje = document.getElementById('mensaje');


    var nombresAleatorios = [
        "Zorro Ágil", "Gato Místico", "Perro Valiente", "Panda Alegre", "Tigre Curioso",
        "Lobo Sabio", "Mono Travieso", "Tortuga Zen", "Águila Real", "Rana Saltarina"
    ];


    function asignarNombreAleatorio() {
        var nombreGenerado = nombresAleatorios[Math.floor(Math.random() * nombresAleatorios.length)];
        nombre.value = nombreGenerado;
        return nombreGenerado;
    }

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
        var nombreUsuario = nombre.value.trim();

   
        if (nombreUsuario === "") {
            nombreUsuario = asignarNombreAleatorio();
        }

        var msg = {
            nombre: nombreUsuario,
            mensaje: mensaje.value
        };

        ws.send(JSON.stringify(msg));
        mensaje.value = "";
    }

    function onMessage(evt){
        var obj = JSON.parse(evt.data);
        var nombreUsuario = obj.nombre;
        var texto = obj.mensaje;
        var icono = '⚫️'; 

        if (nombreUsuario === "Sistema") {
            if (texto.includes("se ha unido")) {
                icono = '✅';
            } else if (texto.includes("ha salido")) {
                icono = '❌';
            } else {
                icono = 'ℹ️';
            }
            mensajes.innerHTML += <br/><em>${icono} ${texto}</em>;
        } else {
            mensajes.innerHTML += <br/>${icono} <strong>${nombreUsuario}:</strong> ${texto};
        }
    }
})(window, document, JSON);
