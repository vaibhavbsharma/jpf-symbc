#===  FUNCTION  ================================================================
#         NAME:  usage
#  DESCRIPTION:  Display usage information.
#===============================================================================
function usage ()
{
    echo "Usage :  $0 [options] [--]

    Options:
    -h|help       Display this message
    -j|jpf_path <jpf_path>   Set jpf_path
    "

}    # ----------  end of function usage  ----------

#-----------------------------------------------------------------------
#  Handle command line arguments
#-----------------------------------------------------------------------

while getopts ":j:h" opt
do
  case $opt in

    h|help     )  usage; exit 0   ;;

    j|jpf_path  )  JPF_DIR="$OPTARG";  ;;

    * )  echo -e "\n  Option does not exist : $OPTARG\n"
                usage; exit 1   ;;

  esac    # --- end of case ---
done
shift $((OPTIND-1))


JR_DIR=$FILEDIR/../

if [ -z $JPF_DIR ]; then
    JPF_DIR=$FILEDIR/../../jpf-core/
    echo "Automatically set JPF_DIR=$JPF_DIR"
fi

if [ ! -e $JPF_DIR  ]; then
    echo "'JPF_DIR=$JPF_DIR' does not exists"
    exit 1
fi



# TODO:
# 1. The code could be organized in a better way.
# 2. Shell is not a good language to organize and reuse code. Some Python CLI tools(e.g. fire) could organize the scripts in a better way
# 3. The scripts could be more automatic(e.g. use `~/.jpf/site.properties` to set the path automatically).  But using shell script to
#    implement it will be too complicated. It will be much better to use Python to implement it.
# 4. A scripts management framework will reduce the scripts maintenance cost.
