export const WEATHER_TYPES = {
  1: { en: "Clear sky", pt: "Céu limpo" },
  2: { en: "Partly cloudy", pt: "Céu pouco nublado" },
  3: { en: "Sunny intervals", pt: "Céu parcialmente nublado" },
  4: { en: "Cloudy", pt: "Céu muito nublado ou encoberto" },
  5: { en: "Cloudy (High cloud)", pt: "Céu nublado por nuvens altas" },
  6: { en: "Showers/rain", pt: "Aguaceiros/chuva" },
  7: { en: "Light showers/rain", pt: "Aguaceiros/chuva fracos" },
  8: { en: "Heavy showers/rain", pt: "Aguaceiros/rain fortes" },
  9: { en: "Rain/showers", pt: "Chuva/aguaceiros" },
  10: { en: "Light rain", pt: "Chuva fraca ou chuvisco" },
  11: { en: "Heavy rain/showers", pt: "Chuva/aguaceiros forte" },
  12: { en: "Intermittent rain", pt: "Períodos de chuva" },
  13: { en: "Intermittent light rain", pt: "Períodos de chuva fraca" },
  14: { en: "Intermittent heavy rain", pt: "Períodos de chuva forte" },
  15: { en: "Drizzle", pt: "Chuvisco" },
  16: { en: "Mist", pt: "Neblina" },
  17: { en: "Fog", pt: "Nevoeiro ou nuvens baixas" },
  18: { en: "Snow", pt: "Neve" },
  19: { en: "Thunderstorms", pt: "Trovoada" },
  20: { en: "Showers and thunderstorms", pt: "Aguaceiros e possibilidade de trovoada" },
  21: { en: "Hail", pt: "Granizo" },
  22: { en: "Frost", pt: "Geada" },
  23: { en: "Rain and thunderstorms", pt: "Chuva e possibilidade de trovoada" },
  24: { en: "Convective clouds", pt: "Nebulosidade convectiva" },
  25: { en: "Partly cloudy", pt: "Céu com períodos de muito nublado" },
  26: { en: "Fog", pt: "Nevoeiro" },
  27: { en: "Cloudy", pt: "Céu nublado" },
  28: { en: "Snow showers", pt: "Aguaceiros de neve" },
  29: { en: "Rain and snow", pt: "Chuva e Neve" },
  30: { en: "Rain and snow", pt: "Chuva e Neve" },
};

// Map weather types to available icons
const WEATHER_ICON_MAP: Record<number, number> = {
  1: 1,   // Clear sky
  2: 2,   // Partly cloudy
  3: 3,   // Sunny intervals
  4: 4,   // Cloudy
  5: 5,   // Cloudy (High cloud)
  6: 6,   // Showers/rain
  7: 7,   // Light showers/rain
  8: 8,   // Heavy showers/rain
  9: 9,   // Rain/showers
  10: 10, // Light rain
  11: 11, // Heavy rain/showers
  12: 12, // Intermittent rain
  13: 13, // Intermittent light rain
  14: 14, // Intermittent heavy rain
  15: 15, // Drizzle
  16: 16, // Mist
  17: 17, // Fog
  18: 18, // Snow
  19: 19, // Thunderstorms
  20: 20, // Showers and thunderstorms
  21: 21, // Hail
  22: 22, // Frost
  23: 23, // Rain and thunderstorms
  24: 24, // Convective clouds
  25: 25, // Partly cloudy
  26: 26, // Fog
  27: 27, // Cloudy
  28: 28, // Snow showers
  29: 29, // Rain and snow
  30: 30, // Rain and snow
};

export function isDayTime(date: Date): boolean {
  const hour = date.getHours();
  return hour >= 6 && hour < 20;
}

export function getWeatherIcon(typeOfWeatherId: number, date: Date, animated: boolean = false): string {
  const timeOfDay = isDayTime(date) ? 'd' : 'n';
  const animationSuffix = animated ? 'anim' : '';
  
  // Map the weather type to an available icon
  const mappedId = WEATHER_ICON_MAP[typeOfWeatherId] || 1; // Default to clear sky if not found
  const paddedId = mappedId.toString().padStart(2, '0');
  
  return `/icons_ipma_weather/w_ic_${timeOfDay}_${paddedId}${animationSuffix}.svg`;
}

export function getWeatherDescription(typeOfWeatherId: number, language: 'en' | 'pt' = 'en'): string {
  return WEATHER_TYPES[typeOfWeatherId as keyof typeof WEATHER_TYPES]?.[language] || 'Unknown weather';
} 