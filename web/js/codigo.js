(function(window, document, JSON){
    'use strict';

    var url = 'ws://' + window.location.host + '/chat_colab/chat_colab',
        ws = new WebSocket(url),
        mensajes = document.getElementById('conversacion'),
        boton = document.getElementById('btnEnviar'),
        nombre = document.getElementById('usuario'),
        mensaje = document.getElementById('mensaje'),
        listaUsuarios = document.getElementById('lista-usuarios');

    var nombreAsignado = "";
    
    var sonidoMensaje = new Audio('recursos/pop.mp3');
    var sonidoCliente = new Audio('recursos/mensaje.mp3');

    const nombresAleatorios = [
        "León", "Tigre", "Elefante", "Cebra", "Jirafa", "Mono", "Puma", "Pantera", "Águila", "Lobo"
    ];

    ws.onopen = () => console.log('Conectado...');
    ws.onclose = () => console.log('Desconectado...');
    boton.addEventListener('click', enviar);

    function enviar(){
        if(!nombreAsignado){
            if(nombre.value.trim() === ""){
                let aleatorio = nombresAleatorios[Math.floor(Math.random() * nombresAleatorios.length)];
                nombreAsignado = aleatorio + Math.floor(Math.random() * 1000);
            } else {
                nombreAsignado = nombre.value.trim();
            }
        }

        var msg = {
            nombre: nombreAsignado,
            mensaje: mensaje.value
        };

        ws.send(JSON.stringify(msg));
        mensaje.value = "";
    }

    function actualizarListaUsuarios(usuarios){
        listaUsuarios.innerHTML = "";
        usuarios.forEach(function(nombreUsuario){
            const li = document.createElement('li');
            li.textContent = '🟢 ' + nombreUsuario;
            listaUsuarios.appendChild(li);
        });
    }

    ws.onmessage = function(evt){
        var obj = JSON.parse(evt.data);

        if(obj.tipo === "mensaje"){
            var msg = '⚫ ' + obj.nombre + ': ' + obj.mensaje;
            mensajes.innerHTML += '<br/>' + msg;
            sonidoMensaje.play();
        } else if(obj.tipo === "sistema"){
            var msg = obj.estado === "entrada"
                ? '✅ ' + obj.nombre + ' se ha unido al chat'
                : '❌ ' + obj.nombre + ' ha salido del chat';
            mensajes.innerHTML += '<br/>' + msg;
            sonidoCliente.play();
        } else if(obj.tipo === "usuarios"){
            actualizarListaUsuarios(obj.lista);
        }
    };

})(window, document, JSON);
