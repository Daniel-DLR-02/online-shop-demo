import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateOrderModal } from './create-order-modal';

describe('CreateOrderModal', () => {
  let component: CreateOrderModal;
  let fixture: ComponentFixture<CreateOrderModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateOrderModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateOrderModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
