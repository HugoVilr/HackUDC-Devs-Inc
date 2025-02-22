import 'package:flutter/material.dart';
import 'dart:async';
import 'package:http/http.dart' as http;
import 'dart:convert';

class BackgroundContainer extends StatelessWidget {
  final Widget child;
  const BackgroundContainer({Key? key, required this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          decoration: const BoxDecoration(
            image: DecorationImage(
              image: AssetImage("assets/fondo.jpg"),
              fit: BoxFit.cover,
            ),
          ),
        ),
        child,
      ],
    );
  }
}

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Aplicacion',
      theme: ThemeData(
        primarySwatch: Colors.red,
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
      ),
      home: SearchScreen(),
      routes: {
        '/results': (context) => ResultsScreen(),
        '/detail': (context) => DetailScreen(),
        '/chat': (context) => ChatScreen(),
      },
    );
  }
}

///////////////////////////////////////////////////////
// Pantalla 1: Buscador
///////////////////////////////////////////////////////
class SearchScreen extends StatefulWidget {
  @override
  _SearchScreenState createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  TextEditingController _controller = TextEditingController();

  void _search() async {
    String query = _controller.text;
    if (query.isEmpty) return;

    try {
      final response = await http.get(
        Uri.parse('http://10.174.249.173:8080/api/trabajadores/buscar?input=$query'),
      );

      if (response.statusCode == 200) {
        List<dynamic> results = json.decode(utf8.decode(response.bodyBytes));
        if (results.isNotEmpty) {
          // Por ejemplo, si deseas navegar a la pantalla de resultados pasando la lista
          Navigator.pushNamed(context, '/results', arguments: results);
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('No se encontraron resultados')),
          );
        }
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Error al obtener los resultados')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return BackgroundContainer(
      child: Scaffold(
        backgroundColor: Colors.transparent,
        resizeToAvoidBottomInset: true,
        floatingActionButton: FloatingActionButton.extended(
          onPressed: () {
            Navigator.pushNamed(context, '/chat');
          },
          label: const Text("IA"),
          icon: const Icon(Icons.chat),
          backgroundColor: Colors.blueAccent,
        ),
        body: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                Image.asset(
                  'assets/logo.png',
                  height: 300,
                  width: 300,
                ),
                const SizedBox(height: 16),
                const Text(
                  'Devs Inc.',
                  style: TextStyle(
                    fontSize: 28,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 32),
                TextField(
                  controller: _controller,
                  decoration: const InputDecoration(
                    hintText: 'Ingresa el término a buscar',
                    prefixIcon: Icon(Icons.search),
                    fillColor: Colors.white,
                    filled: true,
                  ),
                ),
                const SizedBox(height: 16),
                ElevatedButton(
                  onPressed: _search,
                  child: const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                    child: Text(
                      'Buscar',
                      style: TextStyle(fontSize: 18),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

///////////////////////////////////////////////////////
// Pantalla 2: Lista de Resultados (Tarjetas semi-transparentes)
///////////////////////////////////////////////////////
class ResultsScreen extends StatelessWidget {
  Future<Map<String, dynamic>> _fetchUserDetail(String id, BuildContext context) async {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => const Center(child: CircularProgressIndicator()),
    );
    try {
      final response = await http.get(
        Uri.parse('http://10.174.249.173:8080/api/trabajadores/$id'),
      );
      Navigator.pop(context); // Cierra el diálogo
      if (response.statusCode == 200) {
        return json.decode(utf8.decode(response.bodyBytes));
      } else {
        throw Exception('Error al cargar detalles (status: ${response.statusCode})');
      }
    } catch (e) {
      Navigator.pop(context);
      throw Exception('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    final List results = ModalRoute.of(context)!.settings.arguments as List;

    return Scaffold(
      backgroundColor: Colors.transparent,
      appBar: AppBar(
        title: const Text('Resultados'),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      extendBodyBehindAppBar: true,
      body: Stack(
        children: [
          Container(
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage("assets/fondo.jpg"),
                fit: BoxFit.cover,
              ),
            ),
          ),
          results.isNotEmpty
              ? ListView.builder(
            padding: const EdgeInsets.only(top: 100),
            itemCount: results.length,
            itemBuilder: (context, index) {
              var user = results[index];
              String especialidades = (user['especialidades'] as List).join(', ');

              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                elevation: 4,
                color: Colors.white.withAlpha(180),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundImage: AssetImage("assets/user.png"),
                  ),
                  title: Text(user['nombre']),
                  subtitle: Text('Especialidades: $especialidades'),
                  onTap: () async {
                    try {
                      Map<String, dynamic> detail = await _fetchUserDetail(user['id'].toString(), context);
                      Navigator.pushNamed(context, '/detail', arguments: detail);
                    } catch (e) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Error: $e')),
                      );
                    }
                  },
                ),
              );
            },
          )
              : const Center(
            child: Text(
              'No se encontraron resultados',
              style: TextStyle(color: Colors.white, fontSize: 18),
            ),
          ),
        ],
      ),
    );
  }
}

///////////////////////////////////////////////////////
// Pantalla 3: Detalle del Usuario (Ficha con más información)
///////////////////////////////////////////////////////
class DetailScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> userDetail = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    String especialidades = (userDetail['especialidades'] as List).join(', ');

    return BackgroundContainer(
      child: Scaffold(
        backgroundColor: Colors.transparent,
        appBar: AppBar(
          title: const Text('Detalle del Usuario'),
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        extendBodyBehindAppBar: true,
        body: Padding(
          padding: const EdgeInsets.fromLTRB(16, 100, 16, 16),
          child: Card(
            elevation: 8,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
            ),
            color: Colors.white.withAlpha(180),
            child: Padding(
              padding: const EdgeInsets.all(24.0),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Center(
                      child: CircleAvatar(
                        radius: 50,
                        backgroundImage: AssetImage("assets/user.png"),
                      ),
                    ),
                    const SizedBox(height: 16),
                    Center(
                      child: Text(
                        userDetail['nombre'],
                        style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text('Email: ${userDetail['email']}'),
                    const SizedBox(height: 8),
                    Text('Especialidades: $especialidades'),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

///////////////////////////////////////////////////////
// Pantalla 4: Chatbot con IA
///////////////////////////////////////////////////////
class ChatScreen extends StatefulWidget {
  @override
  _ChatScreenState createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  TextEditingController _msgController = TextEditingController();
  List<Map<String, String>> messages = [];
  bool isSending = false;

  // Función para enviar mensaje a la IA
  Future<void> _sendMessage() async {
    String userMessage = _msgController.text.trim();
    if (userMessage.isEmpty) return;

    setState(() {
      messages.add({'role': 'user', 'content': userMessage});
      isSending = true;
      _msgController.clear();
    });

    Map<String, dynamic> payload = {
      "model": "llama3.2:1b",
      "messages": [
        {
          "role": "system",
          "content": "Eres una IA local integrada en Devs Inc.. Devs Inc. es una empresa de desarrollo de software mundialmente reconocida que cuenta con una gran variedad de personal con conocimiento variado, por eso existe esta app. Tu principal objetivo es ayudar a los usuarios a utilizar la aplicación correctamente, guiándolos en la construcción de consultas y proporcionando las palabras clave exactas que deben introducir en el buscador para obtener los resultados más precisos. No tienes acceso directo a la base de datos ni realizas consultas internas. Tu función es actuar como un chatbot de asistencia que interpreta el lenguaje natural del usuario y le devuelve los términos adecuados para buscar perfiles especializados.\n\nSobre la Empresa: Devs Inc. es una empresa tecnológica dedicada a la gestión de talento técnico, utilizando herramientas inteligentes como CompetenciApp para optimizar la búsqueda de profesionales según sus habilidades, titulaciones y experiencia. Su objetivo es conectar proyectos con los expertos adecuados de forma rápida y precisa.\n\nSobre la Aplicación — CompetenciApp: La aplicación permite a los usuarios consultar perfiles técnicos filtrando por especialidades, titulaciones, departamentos y niveles de competencia. A través del buscador, los usuarios pueden encontrar expertos en áreas específicas usando palabras clave predefinidas.\n\nTu función como Chatbot: Cuando el usuario te describa su necesidad en lenguaje natural, debes analizar el contexto y devolverle las palabras clave exactas que debe introducir en el buscador, incluyendo el ID de la especialidad cuando sea relevante. Puedes sugerir combinaciones de términos y filtros para optimizar los resultados.\n\nLista Completa de Especialidades Disponibles con sus IDs:\n1 Desarrollo Web\n2 Programación en Java\n3 Programación en Python\n4 DevOps\n5 Cloud Computing\n6 Infraestructura IT\n7 Seguridad en la Nube\n8 Microservicios\n9 Contenedores y Docker\n10 CI/CD y Automatización\n11 Desarrollo de Aplicaciones Móviles\n12 Bases de Datos y SQL\n13 Big Data y Data Science\n14 Inteligencia Artificial\n15 Machine Learning\n16 Deep Learning\n17 Blockchain y Criptomonedas\n18 Programación Funcional\n19 Programación Orientada a Objetos\n20 Programación Concurrente y Paralela\n21 Testing y QA Automatizado\n22 Desarrollo de Videojuegos\n23 Realidad Virtual y Aumentada\n24 Programación en JavaScript\n25 Desarrollo Frontend\n26 Desarrollo Backend\n27 Seguridad en Aplicaciones Web\n28 Cloud Native y Kubernetes\n29 Internet of Things (IoT)\n30 Arquitectura de Microservicios\n31 Serverless Computing\n32 Automatización de Pruebas\n33 Programación con APIs RESTful\n34 Programación en C# y .NET\n35 Programación en C/C++\n36 Desarrollo de Software Embedded\n37 Desarrollo de Software de Código Abierto\n38 Java\n\nEjemplos de Interacción:\nUsuario: “Busco a alguien experto en Inteligencia Artificial y Machine Learning.”\nIA: “Usa las palabras clave ‘Inteligencia Artificial’ (ID: 14) y ‘Machine Learning’ (ID: 15) en el buscador.”\n\nUsuario: “Necesito un programador con experiencia en desarrollo backend y microservicios.”\nIA: “Introduce ‘Desarrollo Backend’ (ID: 26) y ‘Microservicios’ (ID: 8) en el buscador.”\n\nUsuario: “Quiero un especialista en ciberseguridad para aplicaciones web.”\nIA: “Utiliza la palabra clave ‘Seguridad en Aplicaciones Web’ (ID: 27).”\n\nUsuario: “¿Cómo puedo buscar alguien que trabaje con Docker y Kubernetes?”\nIA: “Introduce ‘Contenedores y Docker’ (ID: 9) y ‘Cloud Native y Kubernetes’ (ID: 28) en el buscador.”\n\nUsuario: “Estoy buscando alguien que sepa programar en C++ y tenga experiencia en software embebido.”\nIA: “Usa ‘Programación en C/C++’ (ID: 35) y ‘Desarrollo de Software Embedded’ (ID: 36) en el buscador.”\n\nReglas que Debes Seguir:\n- No accedas ni modifiques la base de datos. Solo guiás al usuario en la creación de consultas.\n- Siempre utiliza las especialidades con sus nombres y IDs exactos.\n- Si el usuario proporciona términos generales o ambiguos, sugiere las palabras clave más relevantes.\n- Si el usuario desea realizar búsquedas combinadas, indica cómo usar los conectores lógicos “Y” y “O”.\n- En caso de dudas, proporciona ejemplos claros de consultas efectivas.\n\nConsejo al Usuario:\n- Usa lenguaje natural. Ejemplos: “Quiero un experto en Java” o “Busco desarrolladores web con experiencia en seguridad.”\n- Combina términos para búsquedas avanzadas. Ejemplo: “Backend Y Microservicios”\n- Utiliza los conectores “Y” y “O” para filtrar o ampliar los resultados.\n\nResumen: Tu rol es actuar como un asistente inteligente que interpreta lo que el usuario necesita y le devuelve las palabras clave específicas que debe ingresar en el buscador de CompetenciApp. Gracias a ti, los usuarios pueden construir consultas precisas sin necesidad de conocer la estructura interna del sistema."
        },
        {"role": "user", "content": userMessage}
      ],
      "stream": false
    };

    try {
      final response = await http.post(
        Uri.parse('http://10.174.249.173:11434/api/chat'),
        headers: {"Content-Type": "application/json"},
        body: json.encode(payload),
      );

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        // Extrae el mensaje de la
        String iaResponse = data['message']['content'];
        setState(() {
          messages.add({'role': 'assistant', 'content': iaResponse});
          isSending = false;
        });
      } else {
        setState(() {
          isSending = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: ${response.statusCode}')),
        );
      }
    } catch (e) {
      setState(() {
        isSending = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  Widget _buildMessage(Map<String, String> msg) {
    bool isUser = msg['role'] == 'user';
    return Align(
      alignment: isUser ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 4, horizontal: 8),
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: isUser ? Colors.blueAccent.withAlpha(180) : Colors.grey.withAlpha(180),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Text(
          msg['content']!,
          style: const TextStyle(color: Colors.white),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BackgroundContainer(
      child: Scaffold(
        backgroundColor: Colors.transparent,
        appBar: AppBar(
          title: const Text('Chat IA'),
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        extendBodyBehindAppBar: true,
        body: Column(
          children: [
            Expanded(
              child: ListView.builder(
                padding: const EdgeInsets.only(top: 100, bottom: 16),
                itemCount: messages.length,
                itemBuilder: (context, index) => _buildMessage(messages[index]),
              ),
            ),
            Container(
              padding: const EdgeInsets.all(8),
              color: Colors.black.withAlpha(100),
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _msgController,
                      style: const TextStyle(color: Colors.white),
                      decoration: const InputDecoration(
                        hintText: 'Escribe tu mensaje...',
                        hintStyle: TextStyle(color: Colors.white70),
                        border: InputBorder.none,
                      ),
                    ),
                  ),
                  isSending
                      ? const CircularProgressIndicator()
                      : IconButton(
                    icon: const Icon(Icons.send, color: Colors.white),
                    onPressed: _sendMessage,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
