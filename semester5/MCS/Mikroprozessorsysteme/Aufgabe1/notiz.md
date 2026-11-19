/* Muster1
 * Zeitspanne 2500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 2500ms  = 1 534 375
 * Skalierunsgfaktor: 1 534 375 / 2^16 = 23,4127044677734375 -> 24 = {/4} {/6} (oder 32 --> /4 und /8)
 *
 * TA0CTL.ID = /4
 * TA0CTL.IDEX = /6
 * TA0CCR0 = 1 534 375 / 24 = 63.932,291666666666666666666666667 = 63 932 - 1
 *
 * Muster2
 * Zeitspanne 1500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 1500ms  = 920 625
 * Skalierunsgfaktor: 920625 / 2^16 = 14,0476226806640625 -> 15 = 16 = {/4}  {/4}
 *
 * TA0CTL.ID = /4
 * TA0CTL.IDEX = /4
 * TA0CCR0 = 920625 / 16 = 57.539,0625 = 57 539 - 1
 *
 * Muster3
 * Zeitspanne 500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 500ms  = 306 875
 * Skalierunsgfaktor: 306 875 / 2^16 = 4,6825408935546875 -> 5 = 6 = {/2} {/3}
 *
 * TA0CTL.ID = /2
 * TA0CTL.IDEX = /3
 * TA0CCR0 = 306 875 / 6 = 51.145,833333333333333333333333333 = 51 146 - 1
 *
 * Muster4
 * Zeitspanne 2500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 2500ms  = 1 534 375
 * Skalierunsgfaktor: 1 534 375 / 2^16 = 23,4127044677734375 -> 24 = {/4} {/6} (oder 32 --> /4 und /8)
 *
 * TA0CTL.ID = /4
 * TA0CTL.IDEX = /6
 * TA0CCR0 = 1 534 375 / 24 = 63.932,291666666666666666666666667 = 63 932 - 1
 *
* Muster5
 * Zeitspanne 3500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 3500ms  = 2.148.125
 * Skalierunsgfaktor: 2.148.125 / 2^16 =32,7777862548828125 -> 40 = {/8} {/5}
 *
 * TA0CTL.ID = /8
 * TA0CTL.IDEX = /5
 * TA0CCR0 = 2.148.125 / 40 = 53703,125 = 53703 - 1

 Muster6
 * Zeitspanne 4500ms
 * Timerclock 613,75kHZ
 * Teilungsfaktor:613,75 kHz * 4500ms  = 2.761.875
 * Skalierunsgfaktor: 2.761.875 / 2^16 = 42,1428680419921875 -> 48 = {/8} {/6}
 *
 * TA0CTL.ID = /8
 * TA0CTL.IDEX = /6
 * TA0CCR0 = 2.761.875 / 48 = 57539,0625 = 57539 - 1
 */