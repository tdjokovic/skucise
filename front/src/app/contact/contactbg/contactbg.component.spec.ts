import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactbgComponent } from './contactbg.component';

describe('ContactbgComponent', () => {
  let component: ContactbgComponent;
  let fixture: ComponentFixture<ContactbgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ContactbgComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactbgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
