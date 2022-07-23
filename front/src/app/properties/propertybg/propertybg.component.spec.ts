import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertybgComponent } from './propertybg.component';

describe('PropertybgComponent', () => {
  let component: PropertybgComponent;
  let fixture: ComponentFixture<PropertybgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertybgComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertybgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
