import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Observable, shareReplay } from 'rxjs';
import { Product } from '../../../core/models/product.model';
import { OrdersService } from '../../../core/services/orders.services';
import { ProductsService } from '../../../core/services/products.services';

@Component({
  selector: 'app-create-order-modal',
  templateUrl: './create-order-modal.html',
  styleUrls: ['./create-order-modal.scss'],
  imports: [CommonModule, ReactiveFormsModule],

})
export class CreateOrderModalComponent {

  @Output() cancel = new EventEmitter<void>();
  @Output() created = new EventEmitter<void>();


  products$!: Observable<Product[]>;
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private ordersService: OrdersService,
    private productsService: ProductsService
  ) {
    this.form = this.fb.group({
      customerName: ['', Validators.required],
      customerContact: ['', [Validators.required, Validators.email]],
      items: this.fb.array([])
    });
  }




  ngOnInit(): void {
    this.products$ = this.productsService.getAll().pipe(
      shareReplay(1)
    );
    this.addItem();
  }
  onCancel(): void {
    this.cancel.emit();
  }

  onSave(): void {
    if (this.form.invalid) return;

    this.ordersService.create(this.form.value).subscribe(() => {
      this.created.emit();
    });
  }


  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  private createItemGroup(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }


  addItem(): void {
    this.items.push(this.createItemGroup());
  }

  removeItem(index: number): void {
    if (this.items.length > 1) {
      this.items.removeAt(index);
    }
  }

  isProductSelected(productId: string, currentIndex: number): boolean {
    return this.items.controls.some((control, index) =>
      index !== currentIndex &&
      control.get('productId')?.value === productId
    );
  }

  get itemGroups(): FormGroup[] {
    return this.items.controls as FormGroup[];
  }



}
