//localglocalvar.c

#include "function.h"
#include "global.h"

int main(void)
{
    int local = 1;

    local = function(local);
    local = function(global);

    return 0;
}