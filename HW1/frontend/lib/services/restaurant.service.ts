import { api } from './api';
import { Restaurant } from './api';

export const restaurantService = {
  getAll: async () => {
    
    const response = await api.get<Restaurant[]>('/api/v1/restaurant/all');
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<Restaurant>(`/api/v1/restaurant/${id}`);
    return response.data;
  },

  getByName: async (name: string) => {
    const response = await api.get<Restaurant>(`/api/v1/restaurant/name/${name}`);
    return response.data;
  },

  create: async (restaurant: Omit<Restaurant, 'id'>) => {
    const response = await api.post<Restaurant>('/api/v1/restaurant', restaurant);
    return response.data;
  },

  update: async (restaurant: Restaurant) => {
    const response = await api.put<Restaurant>('/api/v1/restaurant', restaurant);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/api/v1/restaurant/${id}`);
  }
}; 