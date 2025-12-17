import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { FINAL_ORDER_STATUSES, Order, OrderStatus } from '../../core/models/order.model';

@Component({
  selector: 'app-order-edit-panel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatIconModule],
  templateUrl: './order-edit-panel.html',
  styleUrls: ['./order-edit-panel.scss']
})
export class OrderEditPanelComponent implements OnChanges {

  @Input()
  order: Order | null = null;

  @Output()
  save = new EventEmitter<Partial<Order>>();

  @Output()
  cancel = new EventEmitter<void>();

  isReadOnly = false;
  hasChanges = false;

  readonly statuses: OrderStatus[] = [
    'CREATED',
    'CONFIRMED',
    'SHIPPED',
    'DELIVERED',
    'CANCELLED',
    'RETURNED'
  ];

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      customerName: [''],
      customerContact: [''],
      status: ['CREATED']
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['order'] && this.order) {
      this.isReadOnly = FINAL_ORDER_STATUSES.includes(this.order.status);

      this.form.patchValue({
        customerName: this.order.customerName,
        customerContact: this.order.customerContact,
        status: this.order.status
      }, { emitEvent: false });

      if (this.isReadOnly) {
        this.form.disable();
      } else {
        this.form.enable();
      }

      this.hasChanges = false;

      this.form.valueChanges.subscribe(values => {
        this.hasChanges =
          values.customerName !== this.order!.customerName ||
          values.customerContact !== this.order!.customerContact ||
          values.status !== this.order!.status;
      });
    }
  }


  onSave(): void {
    if (this.isReadOnly || this.form.invalid || !this.order) {
      return;
    }

    const changes: Partial<Order> = {};

    if (this.form.value.customerName !== this.order.customerName) {
      changes.customerName = this.form.value.customerName!;
    }

    if (this.form.value.customerContact !== this.order.customerContact) {
      changes.customerContact = this.form.value.customerContact!;
    }

    if (this.form.value.status !== this.order.status) {
      changes.status = this.form.value.status!;
    }

    this.save.emit(changes);
  }



  onCancel(): void {
    this.cancel.emit();
  }
}
