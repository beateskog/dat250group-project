import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePollComponent } from './createPoll.component';

describe('CreatePollComponent', () => {
  let component: CreatePollComponent;
  let fixture: ComponentFixture<CreatePollComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreatePollComponent]
    });
    fixture = TestBed.createComponent(CreatePollComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
