#!/bin/bash

##
 # Runs the application
 # ____________________________________________________________________________
 #
 # Created by brightSPARK Labs
 # www.brightsparklabs.com
 ##

# -----------------------------------------------------------------------------
# CONSTANTS
# -----------------------------------------------------------------------------

# base directory of the application
readonly APP_DIR=$(cd `dirname $0`; pwd)

# -----------------------------------------------------------------------------
# FUNCTIONS
# -----------------------------------------------------------------------------

##
 # Prints usage
 #
 # @param $*
 #          error message to print above usage [optional]
 ##
function print_usage_and_exit()
{
  [[ -n $* ]] && echo "ERROR: $*"
  cat <<EOF
USAGE: `basename $0` [options] <asn_schema_file>
       `basename $0` [options] <asn_ber_file>
       `basename $0` [options] <asn_schema_file> <asn_ber_file> <top_level_type>

Where:
  asn_schema_file                the ASN.1 schema file to parse (must end in '.asn')
  asn_ber_file                   the ASN.1 BER file to parse (must end in '.ber')
  top_level_type                 the name of the top level type in the schema file

Options:
  -l <log4j_properties_file>     use the specified the log4j properties file (see $APP_DIR/etc/example-log4j.properties)

EOF
  exit 1
}

# -----------------------------------------------------------------------------
# LOGIC
# -----------------------------------------------------------------------------

# process options
while getopts l: flag
do
  case $flag in
    l)
      LOGGING_PROPERTIES="$OPTARG"
      ;;
    b)
      echo "-b used: $OPTARG";
      ;;
    c)
      echo "-c used";
      ;;
    ?)
      print_usage_and_exit "Invalid option supplied"
      ;;
  esac
done
shift $(( OPTIND - 1 ));

# check pre-conditions
readonly APP_JAR=`find "$APP_DIR/" -maxdepth 1 -name '*.jar' | head -1`
[[ -z $APP_JAR ]] && print_usage_and_exit "Could not find application's JAR file in $APP_DIR"
[[ $# -eq 0 || $# -eq 2 ]] && print_usage_and_exit "Mandatory parameter not supplied"

# include logging properties
[[ -z $LOGGING_PROPERTIES ]] && LOGGING_PROPERTIES="$APP_DIR/etc/example_log4j.properties"
echo "INFO: Using logging configuration from: $LOGGING_PROPERTIES"

# build class path
CLASSPATH=""
for JAR in `find "$APP_DIR/lib" -name '*.jar'`
do
  CLASSPATH=$CLASSPATH:$JAR
done

# execute
readonly CMD="java -cp $APP_JAR$CLASSPATH -Dlog4j.configuration=file:$LOGGING_PROPERTIES com.brightsparklabs.asanti.Asanti $*"
echo "INFO: Executing: $CMD"
$CMD

