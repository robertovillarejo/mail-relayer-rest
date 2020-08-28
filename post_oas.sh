# Variables específicas del proyecto
# APICURIO API usa el término 'design' para referirse a una definición de una API (OpenAPI o Swagger)
# La ruta donde se encuentra la definición de la API
export DESIGN_PATH=./mail-relayer-rest/src/main/resources/swagger/api.json
# El id del diseño de la API en APICURIO
export APICURIO_DESIGN_ID=3
# El nombre de usuario con permisos de edición para actualizar el diseño de la API
export USER_NAME=ci.runner
# El client id para autenticarse
export CLIENT_ID=dev-users

if [ -z "$APICURIO_HOST" ]
then
	echo "Falta el valor de la variable APICURIO_HOST" >/dev/stderr
	exit 400
fi

if [ -z "$APICURIO_CLIENT_SECRET" ]
then
	echo "Falta el valor de la variable APICURIO_CLIENT_SECRET" >/dev/stderr
	exit 400
fi

if [ -z "$APICURIO_USER_PASSWORD" ]
then
	echo "Falta el valor de la variable APICURIO_USER_PASSWORD" >/dev/stderr
	exit 400
fi

# Autenticación para obtener token
echo "Autenticando para obtener token con usuario ${USER_NAME}"
export ACCESS_TOKEN=$(curl --request POST \
  --url https://idm.crip.conacyt.mx/auth/realms/master/protocol/openid-connect/token \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data username=${USER_NAME} \
  --data password=${APICURIO_USER_PASSWORD} \
  --data grant_type=password \
  --data client_id=${CLIENT_ID} \
  --data client_secret=${APICURIO_CLIENT_SECRET} | jq -r '.access_token')

if [ -z "$ACCESS_TOKEN" ]
then
	echo "No se pudo autenticar para obtener el token" >/dev/stderr
	exit 401
fi

# Actualización del diseño
echo "Actualizando el diseño con id ${DESIGN_ID} con el contenido de ${DESIGN_PATH}"
curl --request PUT \
  --url ${APICURIO_HOST}/designs/${APICURIO_DESIGN_ID} \
  --header "authorization: Bearer ${ACCESS_TOKEN}" \
  --header 'content-type: application/json;charset=utf-8' \
  -d @${DESIGN_PATH}

exit
