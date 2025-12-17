import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { API_CONFIG } from './api.config';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private readonly baseUrl = `${API_CONFIG.baseUrl}/products`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl);
  }
}
