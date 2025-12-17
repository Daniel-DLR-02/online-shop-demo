import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderEditPanel } from './order-edit-panel';

describe('OrderEditPanel', () => {
  let component: OrderEditPanel;
  let fixture: ComponentFixture<OrderEditPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderEditPanel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderEditPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
