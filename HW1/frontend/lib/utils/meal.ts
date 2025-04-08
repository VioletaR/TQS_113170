export function getMealType(date: Date): string {
  const hour = date.getHours();
  
  if (hour >= 5 && hour < 11) {
    return "Breakfast";
  } else if (hour >= 11 && hour < 16) {
    return "Lunch";
  } else if (hour >= 16 && hour < 20) {
    return "Afternoon";
  } else {
    return "Dinner";
  }
} 