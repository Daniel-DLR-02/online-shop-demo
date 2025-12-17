import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateOrderRequest } from '../models/create-order.model';
import { Order } from '../models/order.model';
import { Page } from '../models/page.model';
import { API_CONFIG } from './api.config';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  private readonly baseUrl = `${API_CONFIG.baseUrl}/orders`;

  constructor(private http: HttpClient) { }

  getAll(page = 0, size = 10): Observable<Page<Order>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<Order>>(this.baseUrl, { params });
  }

  getById(id: string): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/${id}`);
  }

  create(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.baseUrl, request);
  }

  updateStatus(orderId: string, status: string): Observable<Order> {
    return this.http.patch<Order>(
      `${this.baseUrl}/${orderId}/status`,
      { status }
    );
  }


  updateOrder(orderId: string, payload: {
    customerName: string;
    customerContact: string;
    status: string;
  }): Observable<Order> {
    return this.http.put<Order>(`${this.baseUrl}/${orderId}`, payload);
  }

}
