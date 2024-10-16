import argparse as ap
import dotenv as de
import typing as t
import os
import sys


def _parse_args() -> t.Tuple[str, int, int, int]:
    parser = ap.ArgumentParser()
    parser.add_argument('-e', '--envfilename', default='./.env', help="The *.env file path.")
    parser.add_argument('-db', '--databasecontainerlimit', default=15, help="A memory limit for database (postgres) docker container in percents.")
    parser.add_argument('-b', '--backendcontainerlimit', default=35, help="A memory limit for backend (spring-boot) docker container in percents.")
    parser.add_argument('-f', '--frontendcontainerlimit', default=10, help="A memory limit for frontend (angular) docker container in percents.")
    args: ap.Namespace = parser.parse_args()
    return args.envfilename, args.databasecontainerlimit, args.backendcontainerlimit, args.frontendcontainerlimit

def load_env_file(env_file: str) -> t.Dict[str, str]:
    if os.path.isfile(env_file):
        return de.dotenv_values(env_file)
    else:
        raise FileNotFoundError(f"The file {env_file} does not exist.")

def calculate_memory_limits(_database_limit: int,
                            _backend_limit: int,
                            _frontend_limit: int) -> t.Tuple[int, int, int, int]:
    # Узнаем общий объем оперативной памяти в мегабайтах
    total_mem = os.popen("free -m | awk '/^Mem:/{print $2}'").read().strip()
    total_mem = int(total_mem)

    # Вычисляем размеры для приложений
    postgres_mem = total_mem * _database_limit // 100
    spring_boot_mem = total_mem * _backend_limit // 100
    angular_mem = total_mem * _frontend_limit // 100

    return total_mem, postgres_mem, spring_boot_mem, angular_mem

def update_or_add_env_variable(env_file: str, key: str, value: str):
    # Получаем все переменные окружения из файла
    variables = de.dotenv_values(env_file)

    # Проверяем, существует ли ключ в текущих переменных
    variables[key] = value  # обновление или добавление переменной

    # Записываем обновленные переменные обратно в файл
    with open(env_file, 'w') as file:
        for k, v in variables.items():
            file.write(f"{k}={v}\n")

def main():
    # Парсим аргументы, переданные при запуске скрипта
    _env_filename, _database_limit, _backend_limit, _frontend_limit = _parse_args()
    # Вычисляем размеры памяти
    total_mem, postgres_mem, spring_boot_mem, angular_mem = calculate_memory_limits(
        _database_limit, _backend_limit, _frontend_limit
    )

    # Обновляем или добавляем переменные в .env файл
    update_or_add_env_variable(_env_filename, 'TOTAL_MEM', str(total_mem))
    update_or_add_env_variable(_env_filename, 'POSTGRES_MEM_REQUIRED', str(postgres_mem / 2))
    update_or_add_env_variable(_env_filename, 'POSTGRES_MEM_LIMIT', str(postgres_mem))
    update_or_add_env_variable(_env_filename, 'SPRING_BOOT_MEM_REQUIRED', str(spring_boot_mem / 2))
    update_or_add_env_variable(_env_filename, 'SPRING_BOOT_MEM_LIMIT', str(spring_boot_mem))
    update_or_add_env_variable(_env_filename, 'ANGULAR_MEM_REQUIRED', str(angular_mem / 2))
    update_or_add_env_variable(_env_filename, 'ANGULAR_MEM_LIMIT', str(angular_mem))

    print(f"Updated environment variables in {_env_filename}.")

if __name__ == "__main__":
    main()