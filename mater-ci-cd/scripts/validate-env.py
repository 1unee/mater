import argparse as ap
import dotenv as de
import typing as t
import os


def _parse_args() -> t.Tuple[str, str, str]:
    parser = ap.ArgumentParser()
    parser.add_argument('-e', '--envfilename', default='.env', help="The *.env filename.")
    parser.add_argument('-d', '--deniedvalue', default='DEFAULT', help="The denied value for *.env file.")
    parser.add_argument('-s', '--sslfilename', default='.p12', help="The *.p12 file for using SSL.")
    args: ap.Namespace = parser.parse_args()
    print(f'Parsed scripts arguments: env-filename={args.envfilename}, '
          f'denied-value: {args.deniedvalue}, '
          f'ssl-filename={args.sslfilename}')
    return args.envfilename, args.deniedvalue, args.sslfilename


def validate_env_file(env_file: str, denied_value: str = 'DEFAULT'):
    if os.path.isfile(env_file):
        variables = de.dotenv_values(env_file)
        invalid_keys = [key for key, value in variables.items() if value == denied_value]
        if invalid_keys:
            raise ValueError(f"Found {denied_value} values for keys: {', '.join(invalid_keys)}")
        else:
            print(f"No {denied_value} values found. Validation is success.")
    else:
        raise FileNotFoundError(f"The file {env_file} does not exist.")


def validate_ssl_file(ssl_file: str):
    if not os.path.isfile(ssl_file):
        raise FileNotFoundError(f"The file {ssl_file} does not exist.")


def main():
    _env_filename, _denied_env_value, _ssl_filename = _parse_args()
    validate_ssl_file(_ssl_filename)
    validate_env_file(_env_filename, denied_value=_denied_env_value)


# --------------------------------------------------------------------------------------------------
# script executing below
main()

