#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sched.h>
#include <sys/time.h>
#include <fcntl.h>

#define ITERATIONS 10000

int main(void) {
    // Test System Call
    struct timeval sys_time_before, sys_time_after;
    int sys_fd; //Dateideskriptorvariable für die Datei, die für Systemaufruf geöffnet wird
    float sys_time_sum = 0;

    sys_fd = open("./data.input", O_RDONLY);

    //Ruft die aktuelle Systemzeit ab, bevor und nachdem die Systemaufrufe ausgeführt wurden
    gettimeofday(&sys_time_before, NULL);
    //Führt Systemaufruf mehrmals aus, um durchschnittliche Ausführungszeit zu bestimmen
    for (int i = 0; i < ITERATIONS; i++) {
        read(sys_fd, NULL, 0);
    }
    gettimeofday(&sys_time_after, NULL);

    sys_time_sum = (sys_time_after.tv_sec - sys_time_before.tv_sec) * 1e6 +
                   (sys_time_after.tv_usec - sys_time_before.tv_usec);
    printf("Average time for a system call: %.10f microseconds\n", sys_time_sum / ITERATIONS);

    // Test Context Switch
    struct timeval ctx_time_before, ctx_time_after;
    int first_pipe[2], second_pipe[2];
    cpu_set_t cpu_set;// um Prozesse auf einer bestimmten CPU zu binden
    CPU_ZERO(&cpu_set);
    CPU_SET(0, &cpu_set);
    int ctx_rc;

    if (pipe(first_pipe) < 0 || pipe(second_pipe) < 0) {
        printf("Pipe error\n");
        exit(1);
    }

    ctx_rc = fork(); //Erstellt Kindprozess, der Kontextwechseltest ausführt
    if (ctx_rc < 0) {
        printf("Fork() failed\n");
    } else if (ctx_rc == 0) {
        // Child Process
        //Bindet Prozess an bestimmte CPU, um sicherzustellen, dass Kontextwechsel auf derselben CPU stattfindet
        sched_setaffinity(0, sizeof(cpu_set_t), &cpu_set);

        gettimeofday(&ctx_time_before, NULL);
        for (int i = 0; i < ITERATIONS; i++) {
            write(first_pipe[1], NULL, 0);
            read(second_pipe[0], NULL, 0);
        }
        gettimeofday(&ctx_time_after, NULL);

        printf("Average time for a context switch: %.10f microseconds\n",
               ((ctx_time_after.tv_sec - ctx_time_before.tv_sec) * 1e6 +
                (ctx_time_after.tv_usec - ctx_time_before.tv_usec)) / ITERATIONS);
    } else {
        // Parent Process
        sched_setaffinity(0, sizeof(cpu_set_t), &cpu_set);

        gettimeofday(&ctx_time_before, NULL);
        for (int i = 0; i < ITERATIONS; i++) {
            write(second_pipe[1], NULL, 0);
            read(first_pipe[0], NULL, 0);
        }
        gettimeofday(&ctx_time_after, NULL);

        printf("Average time for a context switch: %.10f microseconds\n",
               ((ctx_time_after.tv_sec - ctx_time_before.tv_sec) * 1e6 +
                (ctx_time_after.tv_usec - ctx_time_before.tv_usec)) / ITERATIONS);
    }

    return 0;
}
