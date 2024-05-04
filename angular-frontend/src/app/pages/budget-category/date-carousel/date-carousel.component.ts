import {Component, OnInit} from '@angular/core';
import {
  HlmCarouselComponent,
  HlmCarouselContentComponent, HlmCarouselItemComponent,
  HlmCarouselNextComponent, HlmCarouselPreviousComponent
} from "@spartan-ng/ui-carousel-helm";

@Component({
  selector: 'app-date-carousel',
  standalone: true,
  imports: [HlmCarouselComponent, HlmCarouselContentComponent,HlmCarouselItemComponent,HlmCarouselNextComponent,HlmCarouselPreviousComponent],
  templateUrl: './date-carousel.component.html',
  styleUrl: './date-carousel.component.css'
})
export class DateCarouselComponent implements OnInit {

  dates: string[] = [];



  ngOnInit() {
    this.dates = this.generateDateArray();
  }

  generateDateArray(): string[] {
    const startDate = new Date(2024, 0); // January 1, 2024
    const endDate = new Date(2030, 11); // December, 2030
    const dates: string[] = [];

    const monthNames = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];

    for (let year = startDate.getFullYear(); year <= endDate.getFullYear(); year++) {
      const startMonth = year === startDate.getFullYear() ? startDate.getMonth() : 0;
      const endMonth = year === endDate.getFullYear() ? endDate.getMonth() : 11;

      for (let month = startMonth; month <= endMonth; month++) {
        dates.push(`${monthNames[month]}, ${year}`);
      }
    }

    return dates;
  }

}
