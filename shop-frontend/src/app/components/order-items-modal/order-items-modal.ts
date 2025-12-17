import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OrderItem } from '../../core/models/order-item.model';

@Component({
  selector: 'app-order-items-modal',
  standalone: true,
  templateUrl: './order-items-modal.html',
  styleUrls: ['./order-items-modal.scss']
})
export class OrderItemsModalComponent {
  @Input({ required: true }) items!: OrderItem[];
  @Output() close = new EventEmitter<void>();
}
