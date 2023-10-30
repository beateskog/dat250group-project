import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyPollsComponent } from './myPolls.component';

describe('MyPollsComponent', () => {
  let component: MyPollsComponent;
  let fixture: ComponentFixture<MyPollsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyPollsComponent]
    });
    fixture = TestBed.createComponent(MyPollsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});