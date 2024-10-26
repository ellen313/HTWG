//localglobalvar.c

int function(int param);

int global = 1;

int main(void)
{
    int local = 1;

    local = function(local); //local wird 4
    local = function(gloabal); //local wird 7

    return 0;
}

int function(int param)
{
    static int private_global = 1;
    int local = param + 1;
    private_global++;
    global = param + 2;
    return local + private_global;
}