(function(window, document, JSON) {
    'use strict';
    
    /*const url = 'ws://' + window.location.host + '/chat_colab/chat_colab';*///para forma remota
    const url = 'ws://' + window.location.host + '/chat_colab/chat_colab';//para forma local
    const ws = new WebSocket(url);

    const mensajes = document.getElementById('conversacion');
    const boton = document.getElementById('btnEnviar');
    const nombre = document.getElementById('usuario');
    const mensaje = document.getElementById('mensaje');
    const listaUsuarios = document.getElementById('lista-usuarios');
    const destinatario = document.getElementById('destinatario');

    let nombreAsignado = "";

    const sonidoMensaje = new Audio('recursos/pop.mp3');
    const sonidoCliente = new Audio('recursos/mensaje.mp3');

    const nombresAleatorios = [
        "León", "Tigre", "Elefante", "Cebra", "Jirafa", "Mono", "Puma", "Pantera", "Águila", "Lobo"
    ];

    ws.onopen = () => console.log('Conectado...');
    ws.onclose = () => console.log('Desconectado...');
    boton.addEventListener('click', enviar);

    function enviar() {
        if (!nombreAsignado) {
            if (nombre.value.trim() === "") {
                let aleatorio = nombresAleatorios[Math.floor(Math.random() * nombresAleatorios.length)];
                nombreAsignado = aleatorio + Math.floor(Math.random() * 1000);
            } else {
                nombreAsignado = nombre.value.trim();
            }
            sessionStorage.setItem("usuario", nombreAsignado);
        }

        let para = "todos";  
        let tipo = "mensaje";  

        if (destinatario && destinatario.value && destinatario.value !== "todos") {
            para = destinatario.value; 
            tipo = "privado";  
        } else {
            tipo = "mensaje"; 
            para = "todos"; 
        }

        const msg = {
            tipo: tipo,
            nombre: nombreAsignado,
            mensaje: mensaje.value,
            para: para
        };

        ws.send(JSON.stringify(msg));
        mensaje.value = "";  
    }

    function actualizarListaUsuarios(usuarios) {
        listaUsuarios.innerHTML = "";
        destinatario.innerHTML = '<option value="todos">Todos</option>';
        usuarios.forEach(function(nombreUsuario) {
            const li = document.createElement('li');
            li.textContent = '🟢 ' + nombreUsuario;
            listaUsuarios.appendChild(li);

            if (nombreUsuario !== nombreAsignado) {
                const option = document.createElement('option');
                option.value = nombreUsuario;
                option.textContent = nombreUsuario;
                destinatario.appendChild(option);
            }
        });
    }

    ws.onmessage = function(evt) {
        const obj = JSON.parse(evt.data);

        console.log("Mensaje recibido:", obj); 

        if (obj.tipo === "mensaje" || obj.tipo === "privado") {
            if (obj.nombre && obj.mensaje) {
                const para = obj.para || "todos";
                const prefijo = obj.tipo === "privado" ? "🔒" : "💬";
                const msg = `${prefijo} De ${obj.nombre} para ${para}: ${obj.mensaje}`;
                mensajes.innerHTML += '<br/>' + msg;
                sonidoMensaje.play();
            } else {
                console.log("Faltan datos en el mensaje recibido:", obj);
            }
        } else if (obj.tipo === "sistema") {
            const msg = obj.estado === "entrada"
                ? '✅ ' + obj.nombre + ' se ha unido al chat'
                : '❌ ' + obj.nombre + ' ha salido del chat';
            mensajes.innerHTML += '<br/>' + msg;
            sonidoCliente.play();
        } else if (obj.tipo === "usuarios") {
            actualizarListaUsuarios(obj.lista);
        }
    };
})(window, document, JSON);
