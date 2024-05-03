import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmIconComponent, provideIcons} from "@spartan-ng/ui-icon-helm";
import {lucideClipboardList, lucideCoins, lucideCreditCard, lucideHome, lucideUserCircle} from "@ng-icons/lucide";
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    HlmButtonDirective,
    HlmIconComponent,
    RouterModule
  ],
  providers: [provideIcons({ lucideCoins, lucideHome, lucideClipboardList, lucideUserCircle, lucideCreditCard })],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent  {
}
