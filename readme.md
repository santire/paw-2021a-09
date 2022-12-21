Credenciales de usuario: 

- Dueño de restaurante
Email: admin@gourmetablewebapp.com  
Contraseña: administrator 

- Cliente
Email: comensal@yopmail.com
Contraseña: comensal

Nota: Preferentemente utilizar como cliente alguna cuenta propia (gmail por ejemplo) ya que yopmail no suele renderizar correctamente los formatos de mailing. 

React:

Para levantar el servidor `cd webapp/spa` y correr `yarn start`

Si no existe el archivo `webapp/spa/env.local` debera crearlo con el siguiente contenido

```
REACT_APP_HOST_API="http://localhost:8080/api"
PORT=3000
```


