import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {InputTextModule} from "primeng/inputtext";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {FormsModule} from "@angular/forms";
import {MultiSelectModule} from "primeng/multiselect";

interface City {
  name: string,
  code: string
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MultiSelectModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  cities!: City[];

  selectedCities!: City[];

  ngOnInit() {
    this.cities = [
      {name: 'New York', code: 'NY'},
      {name: 'Rome', code: 'RM'},
      {name: 'London', code: 'LDN'},
      {name: 'Istanbul', code: 'IST'},
      {name: 'Paris', code: 'PRS'}
    ];
  }
}
