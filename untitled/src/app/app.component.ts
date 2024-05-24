import { Component } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [DialogModule, ButtonModule, InputTextModule, AvatarModule]
})
export class AppComponent {
  visible: boolean = false;

  showDialog() {
    this.visible = true;
  }
}
