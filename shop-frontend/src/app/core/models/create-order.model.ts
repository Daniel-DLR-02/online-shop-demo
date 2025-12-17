export interface CreateOrderItemRequest {
  productId: string;
  quantity: number;
}

export interface CreateOrderRequest {
  customerName: string;
  customerContact: string;
  items: CreateOrderItemRequest[];
}
