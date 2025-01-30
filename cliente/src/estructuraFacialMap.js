export const estructuraFacialMap = {
    OVALADA: "Ovalado",
    REDONDA: "Redondo",
    RECTANGULAR: "Rectangular",
    TRIANGULO_INVERTIDO: "Triángulo Invertido",
    CUADRADO: "Cuadrado",
    TRIANGULO: "Triángulo",
    DIAMANTE: "Diamante",
    CORAZON: "Corazón",
    OVALADO_LARGO: "Ovalado Largo"
  };
  
  // obtener clave del backend desde el nombre en frontend
  export const getBackendKey = (frontendName) => {
    return Object.keys(estructuraFacialMap).find(
      (key) => estructuraFacialMap[key] === frontendName
    );
  };
  
  // obtener nombre de frontend desde la clave del backend
  export const getFrontendName = (backendKey) => {
    return estructuraFacialMap[backendKey] || backendKey;
  };
  