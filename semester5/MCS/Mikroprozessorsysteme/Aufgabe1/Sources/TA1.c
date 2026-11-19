#include <msp430.h>
#include "..\base.h"
#include "TA1.h"
#include "event.h"

#define CNTMAX 5 // Max Wert für die Hysterese

typedef enum {S0, S1} TState; //S0 bedeutet, dass nichts ausgelöst/nicht gedrückt wurde, und S1 bedeutet, dass es als gedrückt erkannt wurde

LOCAL struct {
   Int    count;
   TState state;
} var_btn1,
var_btn2;

LOCAL const struct {
   const UChar  * const port;
   const UChar          mask;
   const TEvent         msg;
}  btn1 = {(UChar *)(&P1IN), BIT0, EVENT_BTN1}, // Taster 1 (P1.0)
   btn2 = {(UChar *)(&P1IN), BIT1, EVENT_BTN2}; // Taster 2 (P1.1)


#pragma FUNC_ALWAYS_INLINE(TA1_init)
GLOBAL Void TA1_init(Void) {

   var_btn1.count  = 0;
   var_btn1.state = S0;
   var_btn2.count   = 0;
   var_btn2.state = S0;

   TA1CTL   = 0; // stop mode, disable and clear flags
   TA1CCTL0 = 0; // no capture mode, compare mode
                 // clear and disable interrupt flag
   TA1CCR0  = 2025 - 1;          // set up Compare Register
   TA1EX0   = TAIDEX_0;        // set up expansion register
   TA1CTL   = TASSEL__ACLK     // 613.75 kHz
            | MC__UP           // Up Mode
            | ID__1            // input divider
            | TACLR            // clear and start Timer
            | TAIE             // enable interrupt
            | TAIFG;
}


#pragma vector = TIMER1_A1_VECTOR
__interrupt Void TIMER1_A1_ISR(Void) {
   /*
    * Der Inhalt der ISR ist zu implementieren
    */


    if (!TSTBIT(*btn1.port, btn1.mask)) {  // Button 1 gedrückt
        if (++var_btn1.count >= CNTMAX - 1) {
            var_btn1.count = CNTMAX - 1;
            if (var_btn1.state == S0) {
                var_btn1.state = S1;
                Event_set(btn1.msg); // Event für Taster 1
                __low_power_mode_off_on_exit();
            }
        }
    } else if (!TSTBIT(*btn2.port, btn2.mask)) {  // Button 2 gedrückt
        if (++var_btn2.count >= CNTMAX - 1) {
            var_btn2.count = CNTMAX - 1;
            if (var_btn2.state == S0) {
                var_btn2.state = S1;
                Event_set(btn2.msg); // Event für Taster 2
                __low_power_mode_off_on_exit();
            }
        }
    } else {
        if (--var_btn1.count < 0) {
            var_btn1.count = 0;
            var_btn1.state = S0;
        }
        if (--var_btn2.count < 0) {
            var_btn2.count = 0;
            var_btn2.state = S0;
        }
    }
    CLRBIT(TA1CTL, TAIFG);


}
