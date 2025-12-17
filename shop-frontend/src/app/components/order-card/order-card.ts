import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { FINAL_ORDER_STATUSES, Order, OrderStatus } from '../../core/models/order.model';
import { OrderItemsModalComponent } from "../order-items-modal/order-items-modal";

@Component({
  selector: 'app-order-card',
  standalone: true,
  imports: [CommonModule, MatIconModule, OrderItemsModalComponent],
  templateUrl: './order-card.html',
  styleUrls: ['./order-card.scss']
})
export class OrderCardComponent implements OnInit {

  @Output()
  edit = new EventEmitter<void>();

  @Input({ required: true })
  order!: Order;


  readonly MAX_VISIBLE_ITEMS = 4;

  itemsModalOpen = false;

  isReadOnly: boolean = false

  expanded = signal<boolean>(false);

  ngOnInit(): void {
    this.isReadOnly = FINAL_ORDER_STATUSES.includes(this.order.status);
  }

  getStatusLabel(status: OrderStatus): string {
    return status.replace('_', ' ');
  }

  onEditClick(): void {
    this.edit.emit();
  }

  get statusClass(): string {
    switch (this.order.status) {
      case 'CREATED':
        return 'status-warning';

      case 'CANCELLED':
      case 'RETURNED':
        return 'status-error';

      case 'CONFIRMED':
      case 'SHIPPED':
      case 'DELIVERED':
        return 'status-success';

      default:
        return '';
    }
  }

  get visibleItems() {
    return this.order.items.slice(0, this.MAX_VISIBLE_ITEMS);
  }

  get hasMoreItems(): boolean {
    return this.order.items.length > this.MAX_VISIBLE_ITEMS;
  }


  openItemsModal() {
    this.itemsModalOpen = true;
  }

  closeItemsModal() {
    this.itemsModalOpen = false;
  }


}
