var tid;
var cnt = parseInt(300);//초기값(초단위)
function counter_init() {
    tid = setInterval("counter_run()", 1000);
}

function counter_reset() {
    clearInterval(tid);
    cnt = parseInt(300);
    counter_init();
}

function counter_run() {
    document.all.counter.innerText = time_format(cnt);
    cnt--;
    if(cnt < 0) {
        clearInterval(tid);
        self.location = "logout";
    }
}
function time_format(s) {
    var nHour=0;
    var nMin=0;
    var nSec=0;
    if(s>0) {
        nMin = parseInt(s/60);
        nSec = s%60;

        if(nMin>60) {
            nHour = parseInt(nMin/60);
            nMin = nMin%60;
        }
    }
    if(nSec<10) nSec = "0"+nSec;
    if(nMin<10) nMin = "0"+nMin;

    return ""+nHour+":"+nMin+":"+nSec;
}