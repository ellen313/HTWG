#include <msp430.h>
#include "..\base.h"
#include "event.h"
#include "TA0.h"


/*
 * Man soll sich eine geeignete Datenstruktur überlegen,
 * die eine laufzeiteffiziente Ausführung der ISR ermöglicht.
 */

#define HIGH 0x8000
#define LOW  0x0000

#define ACKFRQ   613.75  // kHz
#define TICK(t, id, idex)  ((UInt)(((ACKFRQ * t) / (id)) / (idex)) - 1) //Rechnungen mit Wert 2^15


//Datenstrukturen
typedef struct {
    UInt id;
    UInt idex;
    UInt ccr0_init; //CompareReg TA0CCR0
} TimerConfig;

typedef struct {
    const UInt* pattern; //Zeiger auf Muster
    TimerConfig config;
} BlinkMuster;

/*Musterdefinitionen*/
//Rechnungen mit Wert 2^15

//Muster1 {\8} {\5} --> Timer Interrups (Rechnung mit 2^15), werden im TICK mitgegeben
LOCAL const UInt muster1[] = {
   HIGH | TICK( 2000, 8, 5),
   LOW  | TICK( 500, 8, 5),
   0};

//Muster2 {\4} {\4}
LOCAL const UInt muster2[] = {
   HIGH | TICK( 750, 4, 4),
   LOW  | TICK( 750, 4, 4),
   0};

//Muster3 {\2} {\3}
LOCAL const UInt muster3[] = {
   HIGH | TICK( 250, 2, 3),
   LOW  | TICK( 250, 2, 3),
   0};

//Muster4 {\4} {\8}
LOCAL const UInt muster4[] = {
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 1500, 4, 8),
   0};

//Muster5 {\4} {\8}
LOCAL const UInt muster5[] = {
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 1500, 4, 8),
   0};

//Muster6 {\4} {\8}
LOCAL const UInt muster6[] = {
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 500, 4, 8),
   HIGH | TICK( 500, 4, 8),
   LOW  | TICK( 1500, 4, 8),
   0};


//Tabelle für Blinkmuster
LOCAL const BlinkMuster blinkmuster[] = {
    { muster1, {ID__4, TAIDEX_5, 63932 - 1}}, //id und idex korrigiert (wichtig ist, dass TAIDEX ein Wert niedriger ist als der berechneter Teiler, war im Bsp Code so)
    { muster2, {ID__4, TAIDEX_3, 57539 - 1}}, //Werte hier Rechnung mit 2^16
    { muster3, {ID__2, TAIDEX_2, 51146 - 1}},
    { muster4, {ID__4, TAIDEX_5, 63932 - 1}},
    { muster5, {ID__8, TAIDEX_4, 53703 - 1}},
    { muster6, {ID__8, TAIDEX_5, 57539 - 1}},
    { NULL,    {0,     0,     0     }} //Terminator
};


//Globale Variablen
LOCAL const UInt* ptr = NULL;
LOCAL TimerConfig timerconf = { ID__1, TAIDEX_0, 0 };
LOCAL Bool change_muster = FALSE;
LOCAL const UInt* ptr_start = NULL;


GLOBAL Void set_blink_muster(UInt arg) {
    if (arg LT sizeof(blinkmuster) AND blinkmuster[arg].pattern NE NULL) {
        ptr_start = blinkmuster[arg].pattern;
        ptr = ptr_start;
        timerconf = blinkmuster[arg].config;
        change_muster = TRUE;
    }
}

#pragma FUNC_ALWAYS_INLINE(TA0_init)
GLOBAL Void TA0_init(Void) {

   TA0CTL   = 0; // stop mode, disable and clear flags
   TA0CCTL0 = 0; // no capture mode, compare mode
                 // clear and disable interrupt flag
   TA0CCR0  = 0;       // set up Compare Register
   TA0EX0   = TAIDEX_0;     // set up expansion register
   TA0CTL   = TASSEL__ACLK  // 613.75 kHz
            | MC__UP        // Up Mode
            | ID__1         // input divider
            | TACLR         // clear and start Timer
            | TAIE          // enable interrupt
            | TAIFG;        // set interrupt flag

   set_blink_muster(0);
}

#pragma vector = TIMER0_A1_VECTOR
__interrupt Void TIMER0_A1_ISR(Void) {

   /*
    * Der Inhalt der ISR ist zu implementieren
    */

    if (change_muster) {
        TA0CTL = 0;
        TA0EX0 = timerconf.idex;
        TA0CCR0 = timerconf.ccr0_init;
        TA0CTL = TASSEL__ACLK
                 | MC__UP
                 | timerconf.id
                 | TACLR
                 | TAIE;
        change_muster = FALSE;
        }

    if (*ptr EQ 0) {
        ptr = ptr_start; // fallback
    }

    UInt counter = *ptr++;

    if (TSTBIT(counter, HIGH)) {
        SETBIT(P1OUT, BIT2); //LED2 an
    } else {
        CLRBIT(P1OUT, BIT2); //LED2 aus
    }

    TA0CCR0 = counter BAND BNOT HIGH; //& ~


    CLRBIT(TA0CTL, TAIFG);           // clear interrupt flag
    __low_power_mode_off_on_exit();  // restore Active Mode on return
}
