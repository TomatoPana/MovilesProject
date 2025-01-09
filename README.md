# LocalizationReminder

Recordatorios y alarmas basados en la ubicación del dispositivo. Utiliza los siguientes servicios de Firebase:

## Servicios

- Autenticacion por correo
- Autenticacion por Google
- Servicio de Google Maps
- Servicio de Storage para las fotografias
- Servicio de Firestore para la base de datos

## Funcionamiento

El funcionamiento de la aplicacion permite definir un punto en el mapa y permite definir un rango de tolerancia,
dicho rango de tolerancia puede ir desde 50metros hasta 10km. Cuando se selecciona el punto, se puede crear un
recordatorio, con un titulo obligatorio, un cuerpo opcional, y una fotografia opcional que puede ser tomada con la
cámara o una imagen previamente tomada.
Cuando se guarda la nota, se registra la geocerca y un background service escucha la ubicacion actual, si entra en la
geocerca, una notificacion se lanza con el contenido de la nota. Y mientras se este en la geocerca, la notificacion no
se relanza, es hasta que se sale de la geocerca y vuelve a entrar que se envia de nuevo la notificacion.

Ademas de los recordatorios, tambien se pueden definir alarmas que suenan si se cumplen las siguientes condiciones:

- La alarma se encuentra activada
- La alarma se encuentra en el rango de fechas
- La persona se encuentra cerca de un punto definido en un radio determinado

Por ultimo, una vista de mapa, permite visualizar de forma general todas las notas y alarmas definidas junto sus rangos.
