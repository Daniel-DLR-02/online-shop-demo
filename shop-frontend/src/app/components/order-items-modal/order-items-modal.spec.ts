import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderItemsModal } from './order-items-modal';

describe('OrderItemsModal', () => {
  let component: OrderItemsModal;
  let fixture: ComponentFixture<OrderItemsModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderItemsModal]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OrderItemsModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
