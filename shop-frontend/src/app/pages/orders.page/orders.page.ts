import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { OrderCardComponent } from "../../components/order-card/order-card";
import { OrderEditPanelComponent } from "../../components/order-edit-panel/order-edit-panel";
import { Order } from '../../core/models/order.model';
import { OrdersService } from '../../core/services/orders.services';
import { CreateOrderModalComponent } from "./create-order-modal/create-order-modal";

@Component({
  selector: 'app-orders-page',
  standalone: true,
  imports: [CommonModule, OrderCardComponent, OrderEditPanelComponent, CreateOrderModalComponent],
  templateUrl: './orders.page.html',
  styleUrls: ['./orders.page.scss']
})
export class OrdersPage implements OnInit {

  orders = signal<Order[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  selectedOrder = signal<Order | null>(null);
  panelOpen = signal<boolean>(false);
  createModalOpen = signal(false);

  page = signal(0);
  totalPages = signal(0);

  readonly PAGE_SIZE = 10;

  constructor(private ordersService: OrdersService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(page = 0): void {
    this.loading.set(true);

    this.ordersService.getAll(page, this.PAGE_SIZE).subscribe({
      next: (response) => {
        this.orders.set(response.content);
        this.page.set(response.number);
        this.totalPages.set(response.totalPages);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }


  openEditPanel(order: Order): void {
    this.selectedOrder.set(order);
    this.panelOpen.set(true);
  }

  closeEditPanel(): void {
    this.panelOpen.set(false);
    this.selectedOrder.set(null);
  }

  saveOrderChanges(changes: Partial<Order>): void {
    const order = this.selectedOrder();
    if (!order) return;

    const hasCustomerChanges =
      changes.customerName !== undefined ||
      changes.customerContact !== undefined;

    const hasStatusChange =
      changes.status !== undefined &&
      changes.status !== order.status;

    let request$;

    if (hasCustomerChanges) {
      request$ = this.ordersService.updateOrder(order.id, {
        customerName: changes.customerName ?? order.customerName,
        customerContact: changes.customerContact ?? order.customerContact,
        status: changes.status ?? order.status
      });
    } else if (hasStatusChange) {
      request$ = this.ordersService.updateStatus(order.id, changes.status!);
    } else {
      this.closeEditPanel();
      return;
    }

    request$.subscribe({
      next: () => {
        this.closeEditPanel();
        this.loadOrders();
      }
    });
  }


  openCreateModal(): void {
    this.createModalOpen.set(true);
  }

  closeCreateModal(): void {
    this.createModalOpen.set(false);
  }

  onOrderCreated(): void {
    this.closeCreateModal();
    this.loadOrders();
  }

  nextPage(): void {
    if (this.page() < this.totalPages() - 1) {
      this.loadOrders(this.page() + 1);
    }
  }

  previousPage(): void {
    if (this.page() > 0) {
      this.loadOrders(this.page() - 1);
    }
  }


}
