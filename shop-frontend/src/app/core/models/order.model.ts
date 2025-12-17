import { OrderItem } from './order-item.model';

export type OrderStatus =
  | 'CREATED'
  | 'CONFIRMED'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'
  | 'RETURNED';

export const FINAL_ORDER_STATUSES: OrderStatus[] = [
  'DELIVERED',
  'CANCELLED',
  'RETURNED'
];
export interface Order {
  id: string;                // UUID
  customerName: string;
  customerContact: string;
  status: OrderStatus;
  totalAmount: number;
  createdAt: string;         // ISO date string
  updatedAt: string;         // ISO date string
  items: OrderItem[];
}
